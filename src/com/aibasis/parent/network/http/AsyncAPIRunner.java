package com.aibasis.parent.network.http;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import com.aibasis.parent.network.exception.APIException;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class AsyncAPIRunner {

    private Context context;

    public AsyncAPIRunner(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void requestAsync(String url,APIParameters params,String httpMethod,RequestListener listener) {
        (new AsyncAPIRunner.RequestRunner(this.context,url,params,httpMethod,listener)).execute(new Void[1]);
    }

    private static class AsyncTaskResult<T> {
        private T result;
        private APIException error;

        public T getResult() {
            return this.result;
        }

        public APIException getError() {
            return this.error;
        }

        public AsyncTaskResult(T result) {
            this.result = result;
        }

        public AsyncTaskResult(APIException error) {
            this.error = error;
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    static class RequestRunner extends AsyncTask<Void, Void, AsyncAPIRunner.AsyncTaskResult<String>> {

        private final Context context;

        private final String url;

        private final APIParameters params;

        private final String httpMethod;

        private final RequestListener listener;

        public RequestRunner(Context context,String url,APIParameters params,String httpMethod,RequestListener listener) {
            this.context = context;
            this.url = url;
            this.params = params;
            this.httpMethod = httpMethod;
            this.listener = listener;
        }

        @Override
        protected AsyncTaskResult<String> doInBackground(Void... params) {
            try {
                String e = HttpManager.openUrl(this.context, this.url, this.httpMethod, this.params);
                return new AsyncAPIRunner.AsyncTaskResult(e);
            } catch (APIException e) {
                return new AsyncAPIRunner.AsyncTaskResult(e);
            }
        }

        protected void onPostExecute(AsyncAPIRunner.AsyncTaskResult<String> result) {
            APIException exception = result.getError();
            if(exception != null) {
                this.listener.onAPIException(exception);
            } else {
                this.listener.onComplete(result.getResult());
            }

        }
    }

}
