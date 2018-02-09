package com.lss.network.zb.data;

/**
 * Created by li.shensong on 08/02/2018.
 */
public class TickerBean{


    /**
     * ticker : {"vol":"40.463","last":"0.899999","sell":"0.5","buy":"0.225","high":"0.899999","low":"0.081"}
     * date : 1507875747359
     */

    private Ticker ticker;
    private String date;

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class Ticker {
        /**
         * vol : 40.463
         * last : 0.899999
         * sell : 0.5
         * buy : 0.225
         * high : 0.899999
         * low : 0.081
         */

        private float vol;
        private float last;
        private float sell;
        private float buy;
        private float high;
        private float low;

        public float getVol() {
            return vol;
        }

        public void setVol(float vol) {
            this.vol = vol;
        }

        public float getLast() {
            return last;
        }

        public void setLast(float last) {
            this.last = last;
        }

        public float getSell() {
            return sell;
        }

        public void setSell(float sell) {
            this.sell = sell;
        }

        public float getBuy() {
            return buy;
        }

        public void setBuy(float buy) {
            this.buy = buy;
        }

        public float getHigh() {
            return high;
        }

        public void setHigh(float high) {
            this.high = high;
        }

        public float getLow() {
            return low;
        }

        public void setLow(float low) {
            this.low = low;
        }

        @Override
        public String toString() {
            return "Ticker" +
                    " [ vol = " + vol
                    + " last = " + last
                    + " sell = " + sell
                    + " buy = " + buy
                    + " high = " + high
                    + " low = " + low
                    + " ]";
        }
    }

    @Override
    public String toString() {
        return ticker.toString() + " date = " + date;
    }
}
