package org.lookout.expense_share.models.SplitModels;

import org.lookout.expense_share.models.User;

public class PercentageSplit extends Split {
	double percent;

    public PercentageSplit(User user, double amount, double percent) {
        super(user, amount);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
