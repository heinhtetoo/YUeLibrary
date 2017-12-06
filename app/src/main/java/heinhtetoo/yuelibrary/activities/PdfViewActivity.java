package heinhtetoo.yuelibrary.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heinhtetoo.yuelibrary.R;

public class PdfViewActivity extends AppCompatActivity implements OnLoadCompleteListener {

    private static final String IE_NAME = "name";
    private static final String IE_DOWNLOAD_URL = "downloadUrl";

    private static final String TAG = "YU LiB";

    @Bind(R.id.pv_pdf)
    PDFView pdfView;

    @Bind(R.id.layout_try_again)
    LinearLayout layoutTryAgain;

    public ProgressDialog mProgressDialog;

    public static final String PDF = "tha_ngal_chin.pdf";
    String downloadFileName;
    String downloadFileUrl;

    File cacheDir;

    public static Intent newIntent(Context context, String name, String downloadUrl) {
        Intent intent = new Intent(context, PdfViewActivity.class);
        intent.putExtra(IE_NAME, name);
        intent.putExtra(IE_DOWNLOAD_URL, downloadUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        ButterKnife.bind(this, this);

        downloadFileName = getIntent().getStringExtra(IE_NAME);
        downloadFileUrl = getIntent().getStringExtra(IE_DOWNLOAD_URL);

        cacheDir = getApplicationContext().getCacheDir();

        initDownload();
    }

    private void viewFromFile(File file) {
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableAntialiasing(true)
                .onLoad(this)
                .load();
    }

    public void initDownload() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(downloadFileUrl);

        try {
            final File localFile = File.createTempFile(downloadFileName, "pdf");
            showProgress(getString(R.string.file_loading));
            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    pdfView.setVisibility(View.VISIBLE);
                    layoutTryAgain.setVisibility(View.GONE);
                    viewFromFile(localFile);

                    hideProgress();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pdfView.setVisibility(View.GONE);
                    layoutTryAgain.setVisibility(View.VISIBLE);
                    hideProgress();
                    Snackbar.make(pdfView, "Download Failed", Snackbar.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.layout_try_again)
    public void onClickTryAgain() {
        initDownload();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    protected void onDestroy() {
        hideProgress();
        super.onDestroy();
    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(PdfViewActivity.this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
