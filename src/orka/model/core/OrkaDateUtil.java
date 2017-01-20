package orka.model.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class OrkaDateUtil {

	/**
	 * puni string sa stringom iza
	 * 
	 * @param polje
	 *            - ulaz
	 * @param size
	 *            - velicina
	 * @param scim
	 *            - sa cim
	 * @return
	 */
	public static String padRight(String polje, int size, String scim) {

		while (polje.length() < size) {
			polje = polje.concat(scim);
		}
		return polje;

	}

	/**
	 * puni string sa stringom ispred
	 * 
	 * @param polje
	 *            - ulaz
	 * @param size
	 *            - velicina
	 * @param scim
	 *            - sa cim
	 * @return
	 */
	public static String padLeft(String polje, int size, String scim) {

		while (polje.length() < size) {
			polje = scim.concat(polje);
		}
		return polje;

	}

	/**
	 * Mjenja string na odre�enoj poziciji
	 * 
	 * @param ins
	 *            - String koji se dodaje
	 * @param poz
	 *            - Na koju poziciju
	 * @param red
	 *            - U koji string
	 * @param ulijevo
	 *            - Pozicionira u lijevo
	 * @return
	 */
	public static String pad(String ins, int poz, String red, boolean ulijevo) {
		log.debug("******" + ins + "/" + poz + "/" + red + "/" + ulijevo);
		char[] chars = red.toCharArray();
		char[] insert = ins.toCharArray();

		poz = poz - ins.length();
		for (int i = poz; i < ins.length() + poz; i++) {
			chars[i] = insert[i - poz];
		}
		return String.valueOf(chars);
	}



	public static boolean kontrolaOib(String oibFull) {
		if (!oibFull.matches("\\d{11}")) {
			return false;
		}
		String oib = oibFull.substring(0, oibFull.length() - 1);
		String kont = oibFull.substring(oibFull.length() - 1, oibFull.length());
		char[] chars = oib.toCharArray();
		Vector<Integer> veco = new Vector<Integer>();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			veco.add(Integer.valueOf(String.valueOf(c)));
		}

		Iterator<Integer> it = veco.iterator();
		int ostatak = 10;
		while (it.hasNext()) {
			int broj = it.next();
			int zbroj = broj + ostatak;
			int meduOstatak = 0;
			if (zbroj > 10) {
				meduOstatak = zbroj - 10;
			} else {
				meduOstatak = zbroj;
			}

			ostatak = 0;
			if ((meduOstatak * 2) > 11) {
				ostatak = (meduOstatak * 2) - 11;
			} else {
				ostatak = meduOstatak * 2;
			}

		}

		int kontrol = 11 - ostatak;
		if (kontrol == 10)
			kontrol = 0;
		return kontrol == Integer.valueOf(kont);
	}

	/**
	 * metoda danasnji datum:<br>
	 * vraca danasnji datum.<br>
	 * primjer:Fri Sep 08 00:00:00 GMT 2006
	 * 
	 * @return
	 */
	public static Date danasnjiDatum() {
		GregorianCalendar g = new GregorianCalendar();
		g.clear(GregorianCalendar.AM_PM);
		g.clear(GregorianCalendar.HOUR_OF_DAY);
		g.clear(GregorianCalendar.HOUR);
		g.clear(GregorianCalendar.MINUTE);
		g.clear(GregorianCalendar.SECOND);
		g.clear(GregorianCalendar.MILLISECOND);

		return g.getTime();
	}

	/**
	 * Metoda vraca listu 12 objekata u kojima su poceci i krajevi mjeseca u
	 * godini
	 * 
	 * @param datumIzGodine
	 * @return
	 */
	public static List<Object[]> mjeseciUGodiniPocKraj(Date datumIzGodine) {

		List<Object[]> lista = new Vector<Object[]>();

		Date date = pocetakGodine(datumIzGodine);
		for (int i = 1; i < 13; i++) {
			Object[] o = new Object[2];
			o[0] = prviTrenutakUMjesecu(date);
			o[1] = zadnjiTrenutakUMjesecu(date);

			date = datumPlusMjeseci(date, 1);

			lista.add(o);

		}

		return lista;
	}

	public static boolean datumIzmedjuDatuma(Date datum, Date odDatuma, Date doDatuma) {
		if (datum == null) {
			return false;
		}
		if (odDatuma == null) {
			return false;
		}

		if (doDatuma == null) {
			return false;
		}

		if (datum.compareTo(odDatuma) == 0 || datum.compareTo(odDatuma) == 1) {
			if (datum.compareTo(doDatuma) == 0 || datum.compareTo(doDatuma) == -1) {
				return true;
			}
		}

		return false;
	}

	public static int trenutnaGodina() {
		GregorianCalendar g = new GregorianCalendar();
		int trenutnaGodina = g.get(GregorianCalendar.YEAR);
		return trenutnaGodina;
	}

	/**
	 * metoda danasnji kalendar:<br>
	 * vraca kalendar sa danasnjim datumom i vremenom pociscenim vremenom.
	 * Primjer: Thu Nov 08 00:00:00 CET 2007
	 * 
	 * @return
	 */

	public static GregorianCalendar danasnjiKalendar() {
		GregorianCalendar g = new GregorianCalendar();
		g.clear(GregorianCalendar.AM_PM);
		g.clear(GregorianCalendar.HOUR_OF_DAY);
		g.clear(GregorianCalendar.HOUR);
		g.clear(GregorianCalendar.MINUTE);
		g.clear(GregorianCalendar.SECOND);
		g.clear(GregorianCalendar.MILLISECOND);
		return g;
	}

	/**
	 * metoda mjesec:<br>
	 * Kao parametar prima datum a vraca string mjesec<br>
	 * 
	 * @param datum
	 * @return
	 */
	public static String mjesec(Date date) {
		SimpleDateFormat s = new SimpleDateFormat("MM");
		return s.format(date);

	}

	public static int minutesDiff(Date earlierDate, Date laterDate) {
		if (earlierDate == null || laterDate == null)
			return 0;

		return (int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000));
	}

	/**
	 * metoda godina:<br>
	 * Kao parametar prima datum a vraca string godina<br>
	 * 
	 * @param datum
	 * @return
	 */
	public static String godina(Date date) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy");
		return s.format(date);

	}

	/**
	 * metoda kraj dana:<br>
	 * Kao parametar prima datum i tome datumu dodijeljuje<br>
	 * max. vrijednosti sati,minuta,sekundi i milisekundi.<br>
	 * Primjer:Wed Sep 06 23:59:59 GMT 2006
	 * 
	 * @param datum
	 * @return
	 */
	public static Date krajDana(Date datum) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(datum);
		g.set(GregorianCalendar.HOUR_OF_DAY, 23);
		g.set(GregorianCalendar.MINUTE, 59);
		g.set(GregorianCalendar.SECOND, 59);
		g.set(GregorianCalendar.MILLISECOND, 999);

		return g.getTime();
	}

	/**
	 * Vraca pocetno unix vrijeme:<br>
	 * Thu Jan 01 00:00:00 GMT 1970
	 * 
	 * @return
	 */
	public static Date prviDatum() {
		GregorianCalendar g = new GregorianCalendar();
		g.clear();
		return g.getTime();

	}

	/**
	 * Metoda danasnji plus dana:<br>
	 * Kao parametar prima int dana, sto bi znacilo<br>
	 * da metoda dodaje dane danasnjem ako je broj pozitivan,<br>
	 * tj. oduzima ako je negativan.
	 * 
	 * @param dana
	 * @return datum
	 */
	public static Date danasnjiPlusDana(int dana) {

		GregorianCalendar kal = danasnjiKalendar();
		kal.add(GregorianCalendar.DATE, dana);
		return kal.getTime();
	}

	/**
	 * Metoda danasnji plus godina:<br>
	 * Kao parametar prima int godina, sto bi znacilo<br>
	 * da metoda dodaje godine danasnjem ako je broj pozitivan,<br>
	 * tj. oduzima ako je negativan.
	 * 
	 * @param godina
	 * @return datum
	 */
	public static Date danasnjiPlusGodina(int godina) {

		GregorianCalendar kal = danasnjiKalendar();
		kal.add(GregorianCalendar.YEAR, godina);
		return kal.getTime();
	}

	public static Date datumPlusMjeseci(Date date, int mjeseci) {
		GregorianCalendar kal = new GregorianCalendar();
		kal.setTime(date);
		kal.add(GregorianCalendar.MONTH, mjeseci);
		return kal.getTime();
	}

	public static Date datumPlusGodina(Date date, int godina) {
		GregorianCalendar kal = new GregorianCalendar();
		kal.setTime(date);
		kal.add(GregorianCalendar.YEAR, godina);
		return kal.getTime();
	}

	public static Date datumPlusSati(Date date, int sati) {
		GregorianCalendar kal = new GregorianCalendar();
		kal.setTime(date);
		kal.add(GregorianCalendar.HOUR, sati);
		return kal.getTime();
	}

	public static Date datumPlusDana(Date date, int dana) {
		GregorianCalendar kal = new GregorianCalendar();
		kal.setTime(date);
		kal.add(GregorianCalendar.DATE, dana);
		return kal.getTime();
	}

	public static Date datumPlusMinuta(Date date, int min) {
		GregorianCalendar kal = new GregorianCalendar();
		kal.setTime(date);
		kal.add(GregorianCalendar.MINUTE, min);
		return kal.getTime();
	}

	/**
	 * metoda pocetak godine<br>
	 * vraca pocetni datum trenutne godine<br>
	 * primjer:Sun Jan 01 00:00:00 GMT 2006
	 * 
	 * @return
	 */
	public static Date pocetakGodine() {
		GregorianCalendar g = danasnjiKalendar();
		g.clear(GregorianCalendar.MONTH);
		g.clear(GregorianCalendar.DAY_OF_MONTH);
		g.clear(GregorianCalendar.DAY_OF_YEAR);
		g.clear(GregorianCalendar.DAY_OF_WEEK_IN_MONTH);
		g.clear(GregorianCalendar.DATE);
		g.clear(GregorianCalendar.DAY_OF_WEEK);
		g.clear(GregorianCalendar.WEEK_OF_MONTH);
		g.clear(GregorianCalendar.WEEK_OF_YEAR);
		return g.getTime();

	}

	/**
	 * Metoda vraca datum koji je za mjesec dana manji<br>
	 * od danasnjeg.
	 * 
	 * @return
	 */
	public static Date danasnjiMinusMjesecDana() {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(danasnjiDatum());
		if (g.get(GregorianCalendar.MONTH) == 1) {
			g.setTime(pocetakGodine());
		} else {
			g.add(GregorianCalendar.MONTH, -1);
		}
		return g.getTime();
	}

	/**
	 * Metoda vraca datum koji je godinu dana<br>
	 * prije danasnjeg.
	 * 
	 * @return
	 */
	public static Date danasnjiMinusGodinuDana() {
		GregorianCalendar g = danasnjiKalendar();
		g.add(GregorianCalendar.YEAR, -1);

		return g.getTime();
	}

	/**
	 * Metoda vraca datum koji je kraj trenutne godine.
	 * 
	 * @return
	 */
	public static Date krajGodine() {
		GregorianCalendar g = danasnjiKalendar();
		g.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
		g.set(GregorianCalendar.DAY_OF_MONTH, 31);
		return g.getTime();
	}

	public static Date krajGodine(Date datum) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(datum);
		g.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
		g.set(GregorianCalendar.DAY_OF_MONTH, 31);
		return g.getTime();
	}

	public static GregorianCalendar zadnjiDanPrijeMjeseci(int mjeseci) {
		GregorianCalendar g = danasnjiKalendar();

		g.add(GregorianCalendar.MONTH, mjeseci);
		int dan = g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		g.set(GregorianCalendar.DAY_OF_MONTH, dan);

		return g;
	}

	public static GregorianCalendar prviDanPrijeMjeseci(int mjeseci) {
		GregorianCalendar g = danasnjiKalendar();
		int dan = g.getActualMinimum(GregorianCalendar.DAY_OF_MONTH);
		g.set(GregorianCalendar.DAY_OF_MONTH, dan);
		g.add(GregorianCalendar.MONTH, mjeseci);

		return g;
	}

	public static Date prviDanUMjesecuPlusMjeseci(Date datum, int mjeseci) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(datum);
		int dan = g.getActualMinimum(GregorianCalendar.DAY_OF_MONTH);
		g.set(GregorianCalendar.DAY_OF_MONTH, dan);
		g.add(GregorianCalendar.MONTH, mjeseci);

		return g.getTime();
	}

	public static Date zadnjiDanUMjesecuPlusMjeseci(Date datum, int mjeseci) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(datum);
		g.add(GregorianCalendar.MONTH, mjeseci);
		int dan = g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		g.set(GregorianCalendar.DAY_OF_MONTH, dan);

		return g.getTime();
	}

	public static boolean isIstaGodina(Date prvi, Date drugi) {
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(prvi);
		GregorianCalendar g2 = new GregorianCalendar();
		g2.setTime(drugi);
		if (g1.get(GregorianCalendar.YEAR) == g2.get(GregorianCalendar.YEAR)) {
			return true;
		}

		return false;
	}

	public static boolean isIstiMjesec(Date prvi, Date drugi) {
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(prvi);
		GregorianCalendar g2 = new GregorianCalendar();
		g2.setTime(drugi);
		if (g1.get(GregorianCalendar.MONTH) == g2.get(GregorianCalendar.MONTH)) {
			return true;
		}

		return false;
	}

	public static Date danPlusDana(Date date, int dana) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		g.add(GregorianCalendar.DAY_OF_MONTH, dana);
		date = g.getTime();

		return date;
	}

	public static Date parseDate(String dat) {
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		Date datum;
		try {
			datum = sd.parse(dat);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return datum;
	}

	public static int daysBetween(Date odDatuma, Date doDatuma) {
		DateTime odDateTime = new DateTime(odDatuma);
		DateTime doDateTime = new DateTime(doDatuma);

		return Days.daysBetween(odDateTime, doDateTime).getDays();

	}

	public static Date pocetakGodine(int godina) {
		GregorianCalendar g = new GregorianCalendar();
		g.set(GregorianCalendar.YEAR, godina);
		g.set(GregorianCalendar.DAY_OF_YEAR, g.getActualMinimum(GregorianCalendar.DAY_OF_YEAR));
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMinimum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMinimum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMinimum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMinimum(GregorianCalendar.MILLISECOND));
		return g.getTime();
	}

	public static Date pocetakGodine(Date date) {
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(date);
		GregorianCalendar g = new GregorianCalendar();
		g.set(GregorianCalendar.YEAR, g1.get(GregorianCalendar.YEAR));
		g.set(GregorianCalendar.DAY_OF_YEAR, g.getActualMinimum(GregorianCalendar.DAY_OF_YEAR));
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMinimum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMinimum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMinimum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMinimum(GregorianCalendar.MILLISECOND));
		return g.getTime();
	}

	public static Date krajGodine(int godina) {
		GregorianCalendar g = new GregorianCalendar();
		g.set(GregorianCalendar.YEAR, godina);
		g.set(GregorianCalendar.DAY_OF_YEAR, g.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMaximum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMaximum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMaximum(GregorianCalendar.MILLISECOND));
		return g.getTime();

	}

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(OrkaDateUtil.class);

	public static Date pocetakDana(Date date) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		g.set(GregorianCalendar.HOUR_OF_DAY, 0);
		g.set(GregorianCalendar.MINUTE, 0);
		g.set(GregorianCalendar.SECOND, 0);
		g.set(GregorianCalendar.MILLISECOND, 0);
		return g.getTime();
	}

	public static Date naOdredeniDan(String datum) {
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		Date date;
		try {
			date = sd.parse(datum);
		} catch (ParseException e) {
			log.error("GRESKA OBLIK DATUMA treba biti oblik dd.MM.yyyy", e);
			throw new RuntimeException(e);
		}

		return date;

	}

	public static Date prviTrenutakUMjesecu(Date date) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMinimum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMinimum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMinimum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMinimum(GregorianCalendar.MILLISECOND));

		g.set(GregorianCalendar.DAY_OF_MONTH, g.getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
		return g.getTime();
	}

	public static Date zadnjiTrenutakUMjesecu(Date date) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMaximum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMaximum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMaximum(GregorianCalendar.MILLISECOND));
		g.set(GregorianCalendar.DAY_OF_MONTH, g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		return g.getTime();
	}

	public static Date zadnjiTrenutakUMjesecu(String godina, int mjesec) {
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		Date dat2 = null;
		try {
			dat2 = sd.parse("01." + mjesec + "." + godina);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(dat2);
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMaximum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMaximum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMaximum(GregorianCalendar.MILLISECOND));
		g.set(GregorianCalendar.DAY_OF_MONTH, g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		return g.getTime();
	}

	public static Date zadnjiDanUMjesecuUGodini(String godina, int mjesec) {
		int god = Integer.parseInt(godina);

		GregorianCalendar g = new GregorianCalendar();
		g.clear();
		int gMjesec = GregorianCalendar.JANUARY;
		switch (mjesec) {
		case 1:
			gMjesec = GregorianCalendar.JANUARY;
			break;

		case 2:
			gMjesec = GregorianCalendar.FEBRUARY;
			break;

		case 3:
			gMjesec = GregorianCalendar.MARCH;
			break;

		case 4:
			gMjesec = GregorianCalendar.APRIL;
			break;

		case 5:
			gMjesec = GregorianCalendar.MAY;
			break;
		case 6:
			gMjesec = GregorianCalendar.JUNE;
			break;

		case 7:
			gMjesec = GregorianCalendar.JULY;
			break;

		case 8:
			gMjesec = GregorianCalendar.AUGUST;
			break;

		case 9:
			gMjesec = GregorianCalendar.SEPTEMBER;
			break;

		case 10:
			gMjesec = GregorianCalendar.OCTOBER;
			break;
		case 11:
			gMjesec = GregorianCalendar.NOVEMBER;
			break;

		case 12:
			gMjesec = GregorianCalendar.DECEMBER;
			break;

		default:
			break;
		}
		g.set(GregorianCalendar.MONTH, gMjesec);
		g.set(GregorianCalendar.YEAR, god);

		int maxDan = g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		g.set(GregorianCalendar.DAY_OF_MONTH, maxDan);
		return g.getTime();
	}

	public static Date prviTrenutakUGodini(Date date) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMinimum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMinimum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMinimum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMinimum(GregorianCalendar.MILLISECOND));
		g.set(GregorianCalendar.DAY_OF_YEAR, g.getActualMinimum(GregorianCalendar.DAY_OF_YEAR));
		return g.getTime();
	}

	public static Date prviDanUGodini(String godina) {
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		Date datum = OrkaDateUtil.prviTrenutakUGodini(OrkaDateUtil.danasnjiDatum());
		try {
			Date dat = sd.parse("01.01." + godina);
			datum = dat;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return datum;
	}

	public static Date zadnjiTrenutakUGodini(Date date) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		g.set(GregorianCalendar.HOUR_OF_DAY, g.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
		g.set(GregorianCalendar.MINUTE, g.getActualMaximum(GregorianCalendar.MINUTE));
		g.set(GregorianCalendar.SECOND, g.getActualMaximum(GregorianCalendar.SECOND));
		g.set(GregorianCalendar.MILLISECOND, g.getActualMaximum(GregorianCalendar.MILLISECOND));
		g.set(GregorianCalendar.DAY_OF_YEAR, g.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
		return g.getTime();
	}

	public static Date zadnjiDanUGodini(String godina) {
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		Date datum = OrkaDateUtil.prviTrenutakUGodini(OrkaDateUtil.danasnjiDatum());
		try {
			Date dat = sd.parse("31.12." + godina);
			datum = dat;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return datum;
	}

	public static int compareDates(Date datum1, Date datum2) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(datum1);
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(datum2);

		int razGodina = ((Integer) g.get(GregorianCalendar.YEAR)).compareTo(g1.get(GregorianCalendar.YEAR));
		if (razGodina != 0)
			return razGodina;
		int razMjesec = ((Integer) g.get(GregorianCalendar.MONTH)).compareTo(g1.get(GregorianCalendar.MONTH));
		if (razMjesec != 0)
			return razMjesec;

		int razDan = ((Integer) g.get(GregorianCalendar.DAY_OF_MONTH))
				.compareTo(g1.get(GregorianCalendar.DAY_OF_MONTH));
		return razDan;

	}

	public static String getStringFromDate(Date datum, String pattern) {
		SimpleDateFormat sd = new SimpleDateFormat(pattern);

		String vrati = sd.format(datum);

		return vrati;
	}

	/**
	 * Rastavlja red
	 * 
	 * @param delimiter
	 * @param string
	 * @return
	 */
	public static String[] rastavljanje(String delimiter, String string) {

		String[] rastavljeno = null;
		rastavljeno = string.split(delimiter);
		return rastavljeno;

	}

	public static Date getDateFromString(String datum, String pattern) {
		SimpleDateFormat sd = new SimpleDateFormat(pattern);

		Date vrati = null;
		try {
			vrati = sd.parse(datum);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return vrati;

	}

	/**
	 * vraca listu godina (-15) do +( 20) u odnosu na tekucu godinu
	 * 
	 * @return
	 */

	public List<String> getListaGodina() {
		List<String> li = new Vector<String>();

		int j = -15;
		for (int i = 0; i < 35; i++) {

			String trenutnaGodina = OrkaDateUtil.godina(OrkaDateUtil.danasnjiPlusGodina(j));

			li.add(trenutnaGodina);
			j++;

		}

		return li;
	}

}
