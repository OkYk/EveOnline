package fr.guiguilechat.eveonline.programs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import fr.guiguilechat.eveonline.database.DataBase;
import fr.guiguilechat.eveonline.database.esi.ESIMarket;
import fr.guiguilechat.eveonline.database.yaml.LPOffer;
import fr.guiguilechat.eveonline.database.yaml.LPOffer.ItemRef;
import fr.guiguilechat.eveonline.database.yaml.YamlDatabase;

public class LPCorpEvaluator {

	public static class OfferAnalysis {

		public LPOffer offer;
		public double iskPerLP;
		public String offerGroup;

	}

	// adjust BO by removing this taxe
	double markettax = 0.02;

	// only keep orders with isk/lp >= this value.
	double minReturn = 1200;

	int minLP = 500000;


	public final DataBase db;

	public LPCorpEvaluator() {
		this(new YamlDatabase());
	}

	public LPCorpEvaluator(DataBase db) {
		this.db = db;
	}

	public double analyseCorporationOffers(ESIMarket market, String corpName) {
		System.err.println("analyse " + corpName + " on region " + market.region);
		ArrayList<LPOffer> lpos = new ArrayList<>(db.getLPOffers());
		lpos.removeIf(n -> !corpName.equals(n.corporation) || n.requirements.lp == 0);
		System.err.println(" corp has " + lpos.size() + " offers");
		ArrayList<OfferAnalysis> res = analyseoffers(market, lpos);
		double ret = res.size() < 1 ? 0.0 : res.get(0).iskPerLP;
		System.err
		.println(res.size() == 0 ? " none interesting" : " best one is " + ret + " for " + res.get(0).offer.offer_name);
		return ret;

	}

	/**
	 * analyze a list of lp offers and sort them by decreasing is/lp
	 *
	 * @param market
	 *          the market to consider for BO/SO
	 * @param lpos
	 *          lp offers
	 * @return a new list of the corresponding offer analyses.
	 */
	public ArrayList<OfferAnalysis> analyseoffers(ESIMarket market, Iterable<LPOffer> lpos) {

		ArrayList<OfferAnalysis> offers = new ArrayList<>();
		HashSet<Integer> allBOIDs = new HashSet<>();
		HashSet<Integer> allSOIDs = new HashSet<>();
		for (LPOffer lpo : lpos) {
			allBOIDs.add(lpo.product.type_id);
			for (ItemRef e : lpo.requirements.items) {
				allSOIDs.add(e.type_id);
			}
		}

		market.cacheBOs(allBOIDs.stream().mapToInt(i -> i).toArray());

		for (LPOffer lpo : lpos) {
			OfferAnalysis ana = analyse(lpo, market);
			if (ana != null) {
				offers.add(ana);
			}
		}
		Collections.sort(offers, (oa1, oa2) -> (int) Math.signum(oa2.iskPerLP - oa1.iskPerLP));
		groupOrders(offers);
		return offers;
	}

	public static void main(String[] args) {
		LPCorpEvaluator eval = new LPCorpEvaluator();

		LinkedHashMap<String, ESIMarket> markets = new LinkedHashMap<>();
		markets.put("Jita", new ESIMarket(10000002));
		markets.put("Amarr", new ESIMarket(10000043));
		markets.put("Rens", new ESIMarket(10000030));
		markets.put("Dodixie", new ESIMarket(10000032));
		markets.put("Hek", new ESIMarket(10000042));

		HashSet<String> noHSCorps = new HashSet<>(Arrays.asList("Archangels", "Blood Raiders", "Dominations",
				"Frostline Laboratories", "Guardian Angels", "Guristas", "Guristas Production", "Intaki Bank",
				"Intaki Commerce", "Intaki Space Police", "Intaki Syndicate", "Mordu's Legion", "ORE Technologies",
				"Outer Ring Development", "Outer Ring Excavations", "Outer Ring Prospecting", "Salvation Angels",
				"Serpentis Corporation", "Serpentis Inquest", "The Sanctuary", "True Creations", "True Power"));

		ArrayList<LPOffer> lpos = eval.db.getLPOffers();
		lpos.removeIf(lp -> lp.requirements.lp <= 0 || noHSCorps.contains(lp.corporation));
		System.err.println("lp offers loaded");

		for (Entry<String, ESIMarket> me : markets.entrySet()) {
			System.err.println("");
			System.err.println(me.getKey() + " :");
			ArrayList<OfferAnalysis> offers = eval.analyseoffers(me.getValue(), lpos);
			for (OfferAnalysis oa : offers) {
				System.out.println(oa.offer.offer_name + " ( " + oa.offer.requirements.lp + " lp ): " + oa.iskPerLP
						+ " isk/LP ; " + oa.offerGroup);
			}
		}
	}

	/**
	 * make lp offer analysis on a given market.
	 *
	 * @param o
	 *          the lp offer
	 * @param market
	 *          the market for BO/SO
	 * @return a new offernaalysis which contains the data analysis. return null
	 *         if the order interest is < #minReturn
	 */
	public OfferAnalysis analyse(LPOffer o, ESIMarket market) {
		OfferAnalysis ret = new OfferAnalysis();
		ret.offer = o;
		ret.offerGroup = o.corporation;
		int mult = (int) Math.ceil(1.0 * minLP / o.requirements.lp);

		double prodBO = market.getBO(o.product.type_id, o.product.quantity * mult) * (1 - markettax);
		// if the BO-cost / lp is too low, it wont get bigger when taking SO into
		// account.
		if ((prodBO - o.requirements.isk * mult) / o.requirements.lp / mult < minReturn) {
			return null;
		}
		double reqSO = o.requirements.isk * mult;
		market.cacheSOs(o.requirements.items.stream().mapToInt(ir -> ir.type_id).toArray());
		for (ItemRef rq : o.requirements.items) {
			double itemprice = market.getSO(rq.type_id, rq.quantity * mult);
			reqSO += itemprice;
		}
		ret.iskPerLP = (prodBO - reqSO) / o.requirements.lp / mult;
		return ret.iskPerLP >= minReturn ? ret : null;
	}

	/**
	 * group the offers if they have same item, return , ands/lp requirements
	 *
	 * @param offers
	 */
	public static void groupOrders(ArrayList<OfferAnalysis> offers) {
		OfferAnalysis previous = null;
		for (Iterator<OfferAnalysis> it = offers.iterator(); it.hasNext();) {
			OfferAnalysis oa = it.next();
			if (previous != null && previous.iskPerLP == oa.iskPerLP
					&& previous.offer.offer_name.equals(oa.offer.offer_name)
					&& previous.offer.requirements.lp == oa.offer.requirements.lp
					&& previous.offer.requirements.isk == oa.offer.requirements.isk) {
				it.remove();
				previous.offerGroup = previous.offerGroup + ", " + oa.offer.corporation;
			} else {
				previous = oa;
			}
		}
	}

	/**
	 * cached data for a given market
	 *
	 */
	public class MarketLPEvaluator {

		protected final ESIMarket market;

		public MarketLPEvaluator(ESIMarket market) {
			this.market = market;
		}

		private HashMap<String, Double> cachedValues = new HashMap<>();

		public double analyseCorporationOffers(String corpName) {
			if (cachedValues.containsKey(corpName)) {
				return cachedValues.get(corpName);
			}
			double val = LPCorpEvaluator.this.analyseCorporationOffers(market, corpName);
			cachedValues.put(corpName, val);
			return val;
		}

	}

	public MarketLPEvaluator cached(ESIMarket market) {
		return new MarketLPEvaluator(market);
	}
}
