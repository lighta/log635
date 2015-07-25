package log635_lab3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Vector;

public class Normalizer {

	
	protected static Double round(Double value, int nbDec) {
		if (nbDec < 0) nbDec = 0;

		return new BigDecimal(value).setScale(nbDec, RoundingMode.HALF_UP).doubleValue();
	}
	
	static public Vector<Vector<Double>> scale(Vector<Vector<Double>> dataSet, int lowLimit, int upLimit, int nbDec) {
		Double min = 0.0, max = 0.0;
		boolean minMaxInit = true;
		Vector<Vector<Double>> set = new Vector<Vector<Double>>();
		Vector<Double> vline = null;

		for (int i = 1; i < 3; i++) {

			final Iterator<Vector<Double>> colIt = dataSet.iterator();
			while (colIt.hasNext()) {
				final Iterator<Double> lineIt = ((Vector<Double>) colIt.next())
						.iterator();

				if (i == 2)
					vline = new Vector<Double>();

				while (lineIt.hasNext()) {
					final Double val = (Double) lineIt.next();

					if (i == 1) {
						if (minMaxInit) {

							min = val;
							max = val;
							minMaxInit = false;
						} else {
							if (val < min)
								min = val;
							if (val > max)
								max = val;
						}
					} else {

						final Double normVal = lowLimit + ((val - min)*(upLimit-lowLimit))
								/ ((max - min) != 0 ? (max - min) : 1);
						vline.add(round(normVal, 6));

					}
				}

				if (i == 2)
					set.add(vline);
			}
		}

		return set;
	}
}
