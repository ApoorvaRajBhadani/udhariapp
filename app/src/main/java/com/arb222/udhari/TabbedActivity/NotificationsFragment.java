package com.arb222.udhari.TabbedActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arb222.udhari.AddContact.FindActiveUsersActivity;
import com.arb222.udhari.R;

public class NotificationsFragment extends Fragment {
    View view;

    public NotificationsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_notifications,container,false);
        return view;
    }
}
