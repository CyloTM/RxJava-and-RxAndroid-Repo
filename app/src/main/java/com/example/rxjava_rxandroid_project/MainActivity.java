package com.example.rxjava_rxandroid_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.rxjava_rxandroid_project.model.Task;
import com.example.rxjava_rxandroid_project.util.DataSource;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //ui
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.text_view);

        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Throwable {
                        Log.d(TAG, "test: " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(1000);
                            } catch (InterruptedException e){
                            e.printStackTrace();

                        }
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>(){

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: called");
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called");
            }
        });




    }
}