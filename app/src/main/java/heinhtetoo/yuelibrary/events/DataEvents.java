package heinhtetoo.yuelibrary.events;

import java.util.List;

import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.data.vos.StoryVO;

/**
 * Created by Hein Htet Oo on 11/28/2017.
 */

public class DataEvents {

    public static class TabLayoutChangedEvent {
        private String title;

        public TabLayoutChangedEvent(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class BookListLoadedEvent {
        private List<BookVO> bookList;

        public BookListLoadedEvent(List<BookVO> bookList) {
            this.bookList = bookList;
        }

        public List<BookVO> getBookList() {
            return bookList;
        }
    }

    public static class StoryListLoadedEvent {
        private List<StoryVO> storyList;

        public StoryListLoadedEvent(List<StoryVO> storyList) {
            this.storyList = storyList;
        }

        public List<StoryVO> getStoryList() {
            return storyList;
        }
    }

}
