package com.mobiato.sfa.utils;


import android.os.AsyncTask;

import java.util.ArrayList;

public class Chain {

    private ArrayList<Link> links = new ArrayList<>();

    private Link done;
    private Link fail;

    private boolean running;

    public Chain(Link done) {
        this.done = done;
    }

    public void add(Link link) {
        if (!running) {
            links.add(link);
        }
    }

    public void setFail(Link fail) {
        this.fail = fail;
    }

    public void start() {
        running = true;

        new RihmaRunner().execute(links.toArray(new Link[links.size()]));
    }

    public static class Link {
        public void run() throws Exception {
        }
    }

    private class RihmaRunner extends AsyncTask<Link, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Link... params) {

            try {
                Constant.APICOUNT = (Constant.TotalAPICOUNT - links.size()) + "/" + Constant.TotalAPICOUNT;
                params[0].run();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean finished) {
            if (links.size() > 1) {
                links.remove(0);
                start();
            } else {
                links.clear();
                running = false;
                try {
                    if (finished) {
                        done.run();
                    } else {
                        fail.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
