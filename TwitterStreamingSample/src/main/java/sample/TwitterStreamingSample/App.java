package sample.TwitterStreamingSample;

import com.mlkcca.client.DataElementValue;
import com.mlkcca.client.DataStore;
import com.mlkcca.client.MilkCocoa;
import com.mlkcca.client.MilkcocoaException;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class App {
	private static MilkCocoa milkcocoa = null;
	private static DataStore dataStore = null;
	private static DataElementValue dataElementValue = null;

	private static String text;
	private static String id;

	private static class Listener implements StatusListener {
		public void onStatus(Status status) {
			text = status.getText();

			if (text.startsWith("RT", 0)) {
				return;
			}
			id = String.valueOf(status.getId());

			dataElementValue = new DataElementValue();
			dataElementValue.put(id, text);
			dataStore.push(dataElementValue);
			System.out.println("id = " + id + ", text = " + text);
		}

		public void onTrackLimitationNotice(int i) {
			System.out.println("!-----[onTrackLimitationNotice]");
		}

		public void onScrubGeo(long lat, long lng) {
			System.out.println("!-----[onScrubGeo]");
		}

		public void onException(Exception excptn) {
			System.out.println("!-----[onException]");
		}

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			System.out.println("!-----[onDeletionNotice]");
		}

		public void onStallWarning(StallWarning warning) {
			System.out.println("!-----[onStallWarning]");
		}
	}

	public static void main(String[] args) {
		final boolean DEBUG = System.getenv("TWITTER4J_DEBUG").equals("true");
		final String TWITTER_CONSUMER_KEY = System.getenv("TWITTER_CONSUMER_KEY");
		final String TWITTER_CONSUMER_SECRET = System.getenv("TWITTER_CONSUMER_SECRET");
		final String TWITTER_ACCESS_TOKEN = System.getenv("TWITTER_ACCESS_TOKEN");
		final String TWITTER_ACCESS_TOKEN_SECRET = System.getenv("TWITTER_ACCESS_TOKEN_SECRET");
		final String[] TRACK = { "#njslyr", "#DHTPOST", "#DHTLS" };
		// final String[] TRACK = { "猫" };

		final String API_KEY = "";
		final String API_SECRET = "";
		final String APP_ID = "";
		final String HOST = "";
		final String DATASTORE = "";

		try {
			milkcocoa = MilkCocoa.connectWithAPIKey(HOST, APP_ID, API_KEY, API_SECRET, true);
			dataStore = milkcocoa.dataStore(DATASTORE);
		} catch (MilkcocoaException e) {
		}

		Configuration configuration = new ConfigurationBuilder().setDebugEnabled(DEBUG)
				.setOAuthConsumerKey(TWITTER_CONSUMER_KEY).setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
				.setOAuthAccessToken(TWITTER_ACCESS_TOKEN).setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET)
				.build();

		TwitterStream twStream = new TwitterStreamFactory(configuration).getInstance();
		twStream.addListener(new Listener());

		FilterQuery filter = new FilterQuery();
		filter.track(TRACK);
		twStream.filter(filter);
	}

}
