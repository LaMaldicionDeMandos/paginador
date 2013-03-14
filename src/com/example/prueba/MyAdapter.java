package com.example.prueba;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyAdapter extends FragmentPagerAdapter implements ImageProvider{
	private final Context context;
	public MyAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	private final String[] urls = new String[]{
		"https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSXDePGjGXV4u_KpgsZFcAWnALSC7yvs1ZRLmWeEs19bXr9nKX-",
		"https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRgoDkJWGVTDGyXsu_u724FQmxjkAygeLWY5otYfINDA2phPWOv",
		"https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTRfrKQb85e-GwYfUdgiEtQnWqznGoWeNVDXFnLP_QRZWdFUCJJ8w",
		"http://t3.gstatic.com/images?q=tbn:ANd9GcTl6DjOT9skGcd0r1jRL7P-hQPLDNN8XwOPWj_LLHOo4e59QU5c"
	};
			
	@Override
	public Fragment getItem(int position) {
		return MyFragment.newInstance(urls[position], this); 
	}

	@Override
	public int getCount() {
		return urls.length;
	}
	
	public static class MyFragment extends Fragment{
		private String url;
		private ImageProvider provider;
        static MyFragment newInstance(String url, ImageProvider provider) {
            MyFragment f = new MyFragment();
            f.url = url;
            f.provider = provider;
            return f;
        }

		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            ImageView view = new ImageView(getActivity());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageResource(R.drawable.ic_launcher);
            provider.provide(view, url);
            return view;
        }
	}

	@Override
	public void provide(ImageView view, String url) {
		MyAsyncTask task = new MyAsyncTask();
		task.execute(url, view);
	}
	
	private void setImageBitmap(final ImageView view, final Bitmap bitmap){
		((Activity)context).runOnUiThread(new Runnable(){
			@Override
			public void run() {
				view.setImageBitmap(bitmap);
			}
		});
	}
	
	class MyAsyncTask extends AsyncTask<Object, Void, Void>{

		@Override
		protected Void doInBackground(Object... params) {
			Bitmap bm; 
			try {
		            URL aURL = new URL((String)params[0]);
		            InputStream is = (InputStream) aURL.getContent();
		            BufferedInputStream bis = new BufferedInputStream(is);
		            bm = BitmapFactory.decodeStream(bis);
		            bis.close();
		            is.close();
		       } catch (IOException e) {
		           throw new RuntimeException(e);
		       }
			setImageBitmap((ImageView)params[1], bm);
			return null;
		}
		
	}
}
