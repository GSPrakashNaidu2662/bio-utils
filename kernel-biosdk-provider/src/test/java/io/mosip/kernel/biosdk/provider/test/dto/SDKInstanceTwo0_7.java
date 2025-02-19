package io.mosip.kernel.biosdk.provider.test.dto;

import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.core.bioapi.model.KeyValuePair;
import io.mosip.kernel.core.bioapi.model.QualityScore;
import io.mosip.kernel.core.bioapi.model.Response;
import io.mosip.kernel.core.bioapi.model.Score;

public class SDKInstanceTwo0_7 {
	@SuppressWarnings({ "unused" })
	public QualityScore checkQuality(BIR sample, KeyValuePair[] flags) {
		QualityScore qualityScore = new QualityScore();
		qualityScore.setScore(90.0F);
		qualityScore.setAnalyticsInfo(flags);
		qualityScore.setInternalScore(0);
		return qualityScore;
	}

	@SuppressWarnings({ "unused" })
	public Score[] match(BIR sample, BIR[] gallery, KeyValuePair[] flags) {
		Score[] scores = new Score[1];
		Score score = new Score();
		score.setAnalyticsInfo(flags);
		score.setInternalScore(59);
		score.setScaleScore(59.0F);
		scores[0] = score;
		return scores;
	}

	@SuppressWarnings({ "unused" })
	public BIR extractTemplate(BIR sample, KeyValuePair[] flags) {
		return null;
	}

	@SuppressWarnings({ "unused" })
	public Response<BIR[]> segment(BIR sample, KeyValuePair[] flags) {
		return null;
	}
}