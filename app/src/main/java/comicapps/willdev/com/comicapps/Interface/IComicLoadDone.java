package comicapps.willdev.com.comicapps.Interface;

import java.util.List;

import comicapps.willdev.com.comicapps.Model.Comic;

public interface IComicLoadDone {
    void onComicLoadDoneListener(List<Comic> comicList);
}
