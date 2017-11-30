package heinhtetoo.yuelibrary.events;

import java.util.List;

import heinhtetoo.yuelibrary.data.vos.BookVO;

/**
 * Created by Hein Htet Oo on 11/28/2017.
 */

public class DataEvents {

    public static class BookListLoadedEvent {
        private List<BookVO> bookList;

        public BookListLoadedEvent(List<BookVO> bookList) {
            this.bookList = bookList;
        }

        public List<BookVO> getBookList() {
            return bookList;
        }
    }

}
