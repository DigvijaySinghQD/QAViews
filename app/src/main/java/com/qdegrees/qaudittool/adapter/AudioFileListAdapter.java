package com.qdegrees.qaudittool.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qdegrees.qaudittool.R;
import com.qdegrees.qaudittool.activity.PlayAudioFileActivity;

import pl.droidsonroids.gif.GifImageView;


public class AudioFileListAdapter extends RecyclerView.Adapter<AudioFileListAdapter.MenuViewHolder> {

    Context context;
    String[] saCallID,saCallLink,saCallStatus,saCallLocation;
    int pos;

    public AudioFileListAdapter(Context context, String[] saCallID,
                                String[] saCallLink, int pos, String[] saCallStatus,
                                String[] saCallLocation) {
        this.context = context;
        this.saCallID = saCallID;
        this.saCallLink = saCallLink;
        this.pos = pos;
        this.saCallStatus = saCallStatus;
        this.saCallLocation = saCallLocation;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.audio_list_view, null);
        return new MenuViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(MenuViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        //getting the product of the specified position
        final String sCallId = saCallID[position];
        final String sCallLink = saCallLink[position];
        final String sCallStatus = saCallStatus[position];
        final String sLocation = saCallLocation[position];

        if (sCallStatus.equals("1")) {
            viewHolder.tvCallStatus.setBackgroundColor(Color.parseColor("#388A3B"));
            viewHolder.tvCallStatus.setText("Good Call");
        }else if (sCallStatus.equals("2")) {
            viewHolder.tvCallStatus.setBackgroundColor(Color.parseColor("#EF6157"));
            viewHolder.tvCallStatus.setText("Bad Call");
        }else {
            viewHolder.tvCallStatus.setBackgroundColor(Color.WHITE);
        }


        if (pos == position) {
            viewHolder.gifImageView.setVisibility(View.VISIBLE);
            viewHolder.ivPlayAudioFileButton.setVisibility(View.GONE);
        }else {
            viewHolder.gifImageView.setVisibility(View.GONE);
            viewHolder.ivPlayAudioFileButton.setVisibility(View.VISIBLE);
        }


        viewHolder.tvRecordingName.setText(sCallId + " (" + sLocation + ")");
        viewHolder.ivPlayAudioFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAudioFileActivity.playAudiofile(sCallLink,sCallId,position);

            }
        });

        viewHolder.ivFavAudioFileBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ivFavAudioFileFill.setVisibility(View.VISIBLE);
                viewHolder.ivFavAudioFileBorder.setVisibility(View.GONE);
            }
        });


        viewHolder.ivFavAudioFileFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ivFavAudioFileFill.setVisibility(View.GONE);
                viewHolder.ivFavAudioFileBorder.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.ivAddRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.filter_alert_view, null);
                mBuilder.setCancelable(false);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);

                CheckBox checkboxAllSend = mView.findViewById(R.id.checkboxAllSend);
                CheckBox checkboxTLSend = mView.findViewById(R.id.checkboxTLSend);
                CheckBox checkboxQASend = mView.findViewById(R.id.checkboxQASend);
                CheckBox checkboxPartnerSend = mView.findViewById(R.id.checkboxPartnerSend);
                CheckBox checkboxClientSend = mView.findViewById(R.id.checkboxClientSend);

                EditText edtEmailAddressSend = mView.findViewById(R.id.edtEmailAddressSend);
                EditText edtRemarkSendFeedback = mView.findViewById(R.id.edtRemarkSendFeedback);


                // for Close Alert Dailog
                TextView tvCloseFilterView = mView.findViewById(R.id.tvCloseFilterView);
                tvCloseFilterView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                // Submit the remark of Audio File
                Button btnSaveRemarkOnFile = mView.findViewById(R.id.btnSaveRemarkOnFile);
                btnSaveRemarkOnFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sEmailFeedback = edtEmailAddressSend.getText().toString();
                        String sRemarkFeedback = edtRemarkSendFeedback.getText().toString();

                        if (!checkboxAllSend.isChecked() & !checkboxTLSend.isChecked() & !checkboxQASend.isChecked() &
                                !checkboxPartnerSend.isChecked() & !checkboxClientSend.isChecked() ) {
                            Toast.makeText(context, "Please Select Any One Sender!!", Toast.LENGTH_SHORT).show();
                        }else if (sRemarkFeedback.isEmpty()) {
                            Toast.makeText(context, "Please Fill Remark for Call Audio", Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Remark Submitted", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    dialog.dismiss();

                                }
                            },5000);
                        }

                    }
                });

                dialog.show();
                dialog.setCancelable(false);

            }
        });


    }


    @Override
    public int getItemCount() {
        return saCallID.length;
    }


    class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecordingName,tvCallStatus;
        ImageView ivPlayAudioFileButton,ivFavAudioFileBorder,ivFavAudioFileFill,ivAddRemark;

        GifImageView gifImageView;

        public MenuViewHolder(View itemView) {
            super(itemView);

            tvRecordingName = itemView.findViewById(R.id.tvRecordingName);
            ivPlayAudioFileButton = itemView.findViewById(R.id.ivPlayAudioFileButton);
            gifImageView = itemView.findViewById(R.id.gifImageView);
            tvCallStatus = itemView.findViewById(R.id.tvCallStatus);
            ivFavAudioFileBorder = itemView.findViewById(R.id.ivFavAudioFileBorder);
            ivFavAudioFileFill = itemView.findViewById(R.id.ivFavAudioFileFill);
            ivAddRemark = itemView.findViewById(R.id.ivAddRemark);

        }
    }
}