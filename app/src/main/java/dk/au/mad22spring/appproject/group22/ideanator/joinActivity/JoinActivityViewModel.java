package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;

public class JoinActivityViewModel extends ViewModel {


    DatabaseReference myRef;


    public void isCodeValid(canHandleResult caller, String joinCode) {
        if (joinCode == null) caller.isCodeValidResult(false, null);
        else if (joinCode.length() < 3) caller.isCodeValidResult(false, null);
        myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode + "/state");
        myRef.get().addOnCompleteListener(task -> {
            String state = (String) task.getResult().getValue();
            if (state == null) caller.isCodeValidResult(false, null);
            else if (state.contains("LOBBY")) caller.isCodeValidResult(true, null);
            else
                caller.isCodeValidResult(false, IdeainatorApplication.getAppContext().getString(R.string.Gamealreadystarted));
        });
    }

    public interface canHandleResult {
        void isCodeValidResult(boolean isValid, @Nullable String errorMessage);
    }
}
