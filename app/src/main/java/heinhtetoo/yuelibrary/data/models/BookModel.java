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

import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.events.DataEvents;

/**
 * Created by Hein Htet Oo on 11/28/2017.
 */

public class BookModel {

    private static final String BOOKS = "books";

    private static BookModel objInstance;

    public List<BookVO> bookList;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mBookDr;

    public BookModel() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        bookList = new ArrayList<>();
    }

    public static BookModel getInstance() {
        if (objInstance == null) {
            objInstance = new BookModel();
        }
        return objInstance;
    }

    public void loadBooks() {
        mBookDr = mDatabaseReference.child(BOOKS);
        final List<BookVO> bookVOS = new ArrayList<>();
        mBookDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BookVO book = snapshot.getValue(BookVO.class);
                        bookVOS.add(book);
                    }

                    Collections.sort(bookVOS, new Comparator<BookVO>() {
                        @Override
                        public int compare(BookVO b1, BookVO b2) {
                            if (b1.getId() > b2.getId())
                                return -1;
                            else if (b1.getId() < b2.getId())
                                return 1;
                            else
                                return 0;
                        }
                    });

                    DataEvents.BookListLoadedEvent event = new DataEvents.BookListLoadedEvent(bookVOS);
                    EventBus.getDefault().post(event);

                    bookList = bookVOS;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<BookVO> getBookList() {
        if (bookList == null) {
            bookList = new ArrayList<>();
        }
        return bookList;
    }

    public BookVO getBookById(int id) {
        BookVO book = new BookVO();
        for (BookVO bookVO : bookList) {
            if (bookVO.getId() == id) {
                book = bookVO;
            }
        }
        return book;
    }

}
