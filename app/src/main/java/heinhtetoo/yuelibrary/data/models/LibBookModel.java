package heinhtetoo.yuelibrary.data.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import heinhtetoo.yuelibrary.data.vos.LibBookVO;
import heinhtetoo.yuelibrary.events.DataEvents;

/**
 * Created by Hein Htet Oo on 12/6/2017.
 */

public class LibBookModel {

    public static final String LIB_BOOKS = "lib-books";

    private static LibBookModel objInstance;

    public List<LibBookVO> libBookList;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mBookDr;

    public LibBookModel() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        libBookList = new ArrayList<>();
    }

    public static LibBookModel getInstance() {
        if (objInstance == null) {
            objInstance = new LibBookModel();
        }
        return objInstance;
    }

    public void loadLibBooks() {
        mBookDr = mDatabaseReference.child(LIB_BOOKS);
        final List<LibBookVO> LibBookVOS = new ArrayList<>();
        mBookDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LibBookVO book = snapshot.getValue(LibBookVO.class);
                        LibBookVOS.add(book);
                    }

                    Collections.sort(LibBookVOS, new Comparator<LibBookVO>() {
                        @Override
                        public int compare(LibBookVO b1, LibBookVO b2) {
                            if (b1.getId() > b2.getId())
                                return -1;
                            else if (b1.getId() < b2.getId())
                                return 1;
                            else
                                return 0;
                        }
                    });

                    DataEvents.LibBookListLoadedEvent event = new DataEvents.LibBookListLoadedEvent(LibBookVOS);
                    EventBus.getDefault().post(event);

                    libBookList = LibBookVOS;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<LibBookVO> getLibBookList() {
        if (libBookList == null) {
            libBookList = new ArrayList<>();
        }
        return libBookList;
    }

    public LibBookVO getLibBookById(int id) {
        LibBookVO libBook = new LibBookVO();
        for (LibBookVO libBookVO : libBookList) {
            if (libBookVO.getId() == id) {
                libBook = libBookVO;
            }
        }
        return libBook;
    }

}