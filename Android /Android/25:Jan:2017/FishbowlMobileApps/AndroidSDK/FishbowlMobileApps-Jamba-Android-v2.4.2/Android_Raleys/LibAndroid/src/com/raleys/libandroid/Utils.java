package com.raleys.libandroid;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

public final class Utils {
	public final static int DEVICE_PHONE = 1;
	public final static int DEVICE_TABLET_SMALL = 2;
	public final static int DEVICE_TABLET_LARGE = 3;

	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo netInfo = null;

		try {
			ConnectivityManager connec = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			netInfo = connec.getActiveNetworkInfo();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (netInfo != null && netInfo.isAvailable() == true)
			return true;
		else
			return false;
	}

	public static int getDeviceType(DisplayMetrics displayMetrics) {
		int widthInches = (int) (displayMetrics.widthPixels / displayMetrics.xdpi);
		int heightInches = (int) (displayMetrics.heightPixels / displayMetrics.ydpi);
		double size = Math.sqrt((widthInches * widthInches)
				+ (heightInches * heightInches));

		if (size > 9.0) {
			Log.i("Utils", "Screen size = " + String.format("%.2f", size)
					+ ", Device type = DEVICE_TABLET_LARGE");
			return DEVICE_TABLET_LARGE;
		} else if (size > 6.0) {
			Log.i("Utils", "Screen size = " + String.format("%.2f", size)
					+ ", Device type = DEVICE_TABLET_SMALL");
			return DEVICE_TABLET_SMALL;
		} else {
			Log.i("Utils", "Screen size = " + String.format("%.2f", size)
					+ ", Device type = DEVICE_PHONE");
			return DEVICE_PHONE;
		}
	}

	// this only works after the Activity's screen is visible
	public static int getStatusBarHeight(Activity activity) {
		Rect outerRect = new Rect();
		Rect usableRect = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getGlobalVisibleRect(outerRect);
		window.getDecorView().getWindowVisibleDisplayFrame(usableRect);
		int statusBarHeight = usableRect.top - outerRect.top;
		// Sometimes, status bar is at bottom...
		int bottomGap = outerRect.bottom - usableRect.bottom;

		if (bottomGap > statusBarHeight)
			statusBarHeight = bottomGap;

		return statusBarHeight;
	}

	public static int getFontSize(int width, int height, Typeface typeface,
			String text) {
		Paint paint = new Paint();
		paint.setTypeface(typeface);
		Rect textBounds = new Rect();
		int maxSize = 0;
		String testText;

		testText = text + "g"; // insure there is text that goes beneath the
								// baseline or the text bounds aren't calculated
								// correctly

		for (maxSize = 1; maxSize < 1000; maxSize++) {
			paint.setTextSize(maxSize);
			Paint.FontMetrics fontMetrics = paint.getFontMetrics();
			paint.getTextBounds(testText, 0, testText.length(), textBounds);

			if (textBounds.width() > width
					|| textBounds.height() > (height - fontMetrics.bottom)) // adds
																			// space
																			// above
																			// text
																			// to
																			// compensate
																			// for
																			// text
																			// below
																			// baseline
				return maxSize - 1;
		}

		return maxSize;
	}

	public static int getFontSizeByHeight(int height, Typeface typeface) {
		Paint paint = new Paint();
		paint.setTypeface(typeface);
		Rect textBounds = new Rect();
		int maxSize = 0;
		String testText = "Tg"; // covers max height above and below baseline

		for (maxSize = 1; maxSize < 1000; maxSize++) {
			paint.setTextSize(maxSize);
			Paint.FontMetrics fontMetrics = paint.getFontMetrics();
			paint.getTextBounds(testText, 0, testText.length(), textBounds);

			if (textBounds.height() > (height - fontMetrics.bottom)) // adds
																		// space
																		// above
																		// text
																		// to
																		// compensate
																		// for
																		// text
																		// below
																		// baseline
				return maxSize - 1;
		}

		return maxSize;
	}

	public static int getTextTopAlignedYOrigin(Paint paint, Rect rect) {
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		// float unused = (fontMetrics.top - fontMetrics.bottom) -
		// (fontMetrics.ascent - fontMetrics.descent);
		// Log.i("FONT", "top:" + fontMetrics.top + ", bottom:" +
		// fontMetrics.bottom + ", ascent:" + fontMetrics.ascent + ", descent:"
		// + fontMetrics.descent + ", leading:" + fontMetrics.leading);
		// Log.i("FONT", "unused = " + unused + ", yOrigin = " + ((int)(rect.top
		// - fontMetrics.top - unused)) + ", rect.bottom = " + rect.bottom);
		// return ((int)(rect.top - fontMetrics.top + (unused / 2))); //
		// fontMetrics.top is negative(calculated from baseline)
		return ((int) (rect.top - fontMetrics.ascent + fontMetrics.leading)); // fontMetrics.top
																				// is
																				// negative(calculated
																				// from
																				// baseline)
	}

	public static int getTextCenteredYOrigin(Paint paint, Rect rect) {
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		float unused = (fontMetrics.top - fontMetrics.bottom)
				- (fontMetrics.ascent - fontMetrics.descent);
		return ((int) (rect.top - fontMetrics.top + (fontMetrics.bottom) - (unused * 2))); // fontMetrics.top
																							// is
																							// negative(calculated
																							// from
																							// baseline)
	}

	public static int getCenteredTextXOrigin(Paint paint, Rect rect, String text) {
		Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		return rect.left + ((rect.right - rect.left) / 2)
				- ((textBounds.right - textBounds.left) / 2);
	}

	public static String autoTruncate(Paint paint, int width, String input) {
		if (input == null || input.length() == 0)
			return "";

		Rect bounds = new Rect();
		int startIndex = 0;

		for (int i = 0; i < input.length(); i++) {
			paint.getTextBounds(input, startIndex, i, bounds);

			if (bounds.width() > width) {
				input = input.substring(0, i - 2) + "...";
				break;
			}
		}

		/*
		 * using paint.breakText() sometimes returns a different length than
		 * paint.getTextBounds() above, can cause problems with components that
		 * use wrapText() below float[] f = new float[10]; int drawableChars =
		 * paint.breakText(input, true, width, f);
		 * 
		 * if(drawableChars < input.length()) input = input.substring(0,
		 * drawableChars - 1) + "...";
		 */

		return input;
	}

	public static ArrayList<String> wrapText(String text, int width, Paint paint) {
		ArrayList<String> result = new ArrayList<String>();
		Rect bounds = new Rect();
		String line = null;
		int startIndex = 0;
		int lastValidIndex = 0;
		try {
			for (int i = 0; i < text.length(); i++) {
				if (i < text.length() - 1) {
					if (text.charAt(i) == ' ') {
						paint.getTextBounds(text, startIndex, i, bounds);

						if (bounds.width() >= width) {
							line = text.substring(startIndex,
									lastValidIndex + 1);
							result.add(line);
							startIndex = lastValidIndex + 1;
						}

						lastValidIndex = i;
					}
				} else // end of text
				{
					paint.getTextBounds(text, startIndex, text.length() - 1,
							bounds);

					if (bounds.width() < width) // last text segment fits on one
												// line
					{
						line = text.substring(startIndex, text.length());
						result.add(line);
					} else // last text segment needs two lines
					{
						// first line
						line = text.substring(startIndex, lastValidIndex + 1);
						result.add(line);
						startIndex += line.length();
						// second line
						line = text.substring(startIndex, text.length());
						result.add(line);
					}
				}
			}
		} catch (Exception e) {
			Log.e("Utils.java - wrapText()", "error");

		}

		return result;
	}

	public static String encodeBase64(String input) {
		byte[] data = null;

		try {
			data = input.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}

		String result = new String(Base64.encodeToString(data, Base64.DEFAULT));
		return result;
	}

	public static DefaultHttpClient getNewHttpClient(int timeout) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			NoCertSSLSocketFactory sf = new NoCertSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(new AllowAllHostnameVerifier());

			HttpParams httpParameters = new BasicHttpParams();
			HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			HttpConnectionParams.setSoTimeout(httpParameters, timeout);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					httpParameters, registry);

			return new DefaultHttpClient(ccm, httpParameters);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static String stringFromHttpEntity(HttpEntity entity) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			entity.writeTo(os);
			os.close();
			return os.toString();
		} catch (Exception ex) {
			Log.e("Utils", "stringFromHttpEntity Exception: " + ex.toString());
			return null;
		}
	}

	public static boolean validateEmail(String email) {
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}

	public static void clearLog() {
		try {
			Runtime.getRuntime().exec(new String[] { "logcat", "-c" });
		} catch (IOException e) {
		}
	}

	public static String readLog() {
		Process mLogcatProc = null;
		BufferedReader reader = null;
		StringBuilder log = new StringBuilder();

		try {
			mLogcatProc = Runtime.getRuntime().exec(
					new String[] { "logcat", "-d", "*:W" });
			reader = new BufferedReader(new InputStreamReader(
					mLogcatProc.getInputStream()));
			String line;
			String separator = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				log.append(line);
				log.append(separator);
			}

			// do whatever you want with the log. I'd recommend using Intents to
			// create an email
		}

		catch (IOException e) {
			; // ...
		}

		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					;
				}
			}
		}

		return log.toString();
	}
}
