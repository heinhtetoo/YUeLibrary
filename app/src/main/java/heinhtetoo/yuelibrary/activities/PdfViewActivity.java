package heinhtetoo.yuelibrary.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import heinhtetoo.yuelibrary.R;

public class PdfViewActivity extends AppCompatActivity implements OnLoadCompleteListener {

    private static final String IE_NAME = "name";
    private static final String IE_DOWNLOAD_URL = "downloadUrl";

    @Bind(R.id.pv_pdf)
    PDFView pdfView;

    public static final String PDF = "tha_ngal_chin.pdf";
    String pdfFileName;


    public static Intent newIntent(Context context,String name, String downloadUrl) {
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

        String fileName = getIntent().getStringExtra(IE_NAME);
        String downloadUrl = getIntent().getStringExtra(IE_DOWNLOAD_URL);

        viewFromAssets(PDF);
    }

    private void viewFromAssets(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(PDF)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .onLoad(this)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {

    }
}
