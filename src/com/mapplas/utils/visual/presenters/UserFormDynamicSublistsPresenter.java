package com.mapplas.utils.visual.presenters;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.user.UserAppAdapter;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;

public class UserFormDynamicSublistsPresenter {

	private UserFormLayoutComponents layoutComponents;

	private ListView list;

	private Context context;

	private User user;
	
	private AppOrderedList appOrderedList;
		
	public UserFormDynamicSublistsPresenter(UserFormLayoutComponents layoutComponents, ListView list, Context context, User user, String location, AppOrderedList appOrderedList) {
		this.layoutComponents = layoutComponents;
		this.list = list;
		this.context = context;
		this.user = user;
		this.appOrderedList = appOrderedList;
	}

	public void present() {
		this.setBlocksLayoutBehaviour();
		this.pinUpsLayoutBehaviour();
	}

	private void pinUpsLayoutBehaviour() {
//		this.layoutComponents.pinUpsLayout().setBackgroundResource();

		this.layoutComponents.pinUpsLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerButtonsLayout());

//				layoutComponents.blocksLayout().setBackgroundResource(Color.TRANSPARENT);
//				layoutComponents.pinUpsLayout().setBackgroundResource(Color.BLUE);

				UserAppAdapter ula = new UserAppAdapter(context, R.id.lblTitle, user.pinnedApps(), UserAppAdapter.PINUP, user, true, appOrderedList);
				list.setAdapter(ula);
			}
		});
	}

	private void setBlocksLayoutBehaviour() {
		this.layoutComponents.blocksLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerButtonsLayout());

//				layoutComponents.pinUpsLayout().setBackgroundResource(Color.TRANSPARENT);
//				layoutComponents.blocksLayout().setBackgroundResource(Color.BLUE);

				UserAppAdapter ula = new UserAppAdapter(context, R.id.lblTitle, user.blockedApps(), UserAppAdapter.BLOCK, user, true, appOrderedList);
				list.setAdapter(ula);
			}
		});
	}

}
