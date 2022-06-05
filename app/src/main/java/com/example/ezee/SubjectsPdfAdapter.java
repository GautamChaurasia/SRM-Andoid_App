package com.example.ezee;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezee.syllabusFrag.WebFragment;

import java.util.List;

public class SubjectsPdfAdapter extends RecyclerView.Adapter<SubjectsPdfAdapter.myviewholder> {
    List<SyllabusPdfModel> data;

    public SubjectsPdfAdapter(List<SyllabusPdfModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row_pdf,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.syllabusPdfTitle.setText(data.get(position).getSem());
        Toast.makeText(holder.syllabusPdfTitle.getContext(), data.get(position).getFileurl(), Toast.LENGTH_LONG).show();

        holder.syllabusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.syllabusCard.getContext(),SpdfActivity.class);
                intent.putExtra("filename",data.get(position).getFilename());
                intent.putExtra("fileurl",data.get(position).getFileurl());
                System.out.println("----FILENAME AND FILEURL----" + data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.syllabusCard.getContext().startActivity(intent);
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.pdfListContainer,new WebFragment(data.get(position).getFileurl(),data.get(position).getSem())).commit();
            }
        });

        holder.syllabusPdfDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(holder.syllabusPdfDownload.getContext(),data.get(position).getFilename(),".pdf", Environment.DIRECTORY_DOWNLOADS,data.get(position).getFileurl());
            }
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationExtension, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationExtension, fileName+fileExtension);

        downloadManager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {

        CardView syllabusCard;
        ImageView syllabusPdfDownload;
        TextView syllabusPdfTitle;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            syllabusPdfDownload = itemView.findViewById(R.id.sylDownloadPdf);
            syllabusPdfTitle = itemView.findViewById(R.id.sylPdfTitle);
            syllabusCard = itemView.findViewById(R.id.syllabusCard);
        }
    }

}
