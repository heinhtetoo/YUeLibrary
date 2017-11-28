package heinhtetoo.yuelibrary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.shockwave.pdfium.PdfDocument;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PdfViewActivity extends AppCompatActivity implements OnLoadCompleteListener {

    @Bind(R.id.pv_pdf)
    PDFView pdfView;

    public static final String PDF = "tha_ngal_chin.pdf";
    String pdfFileName;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PdfViewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        ButterKnife.bind(this, this);

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
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        setTitle(meta.getTitle() + "");
    }
}
