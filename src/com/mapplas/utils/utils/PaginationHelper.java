package com.mapplas.utils.utils;

import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;

public class PaginationHelper {

	public int checkPageToRequest(boolean resetPagination, SuperModel model) {
		int page = 0;

		if(!resetPagination) {
			int loadedApps = model.appList().size();

			if(loadedApps % Constants.MAPPLAS_APPLICATION_APPS_PAGINATION_NUMBER == 0) {
				page = loadedApps / Constants.MAPPLAS_APPLICATION_APPS_PAGINATION_NUMBER;
			}
			else {
				page = (loadedApps / Constants.MAPPLAS_APPLICATION_APPS_PAGINATION_NUMBER) + 1;
			}
		}

		return page;
	}
}
