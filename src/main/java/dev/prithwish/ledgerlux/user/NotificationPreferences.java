package dev.prithwish.ledgerlux.user;

public class NotificationPreferences {
    private boolean budgetAlerts;
    private boolean goalReminders;
    private boolean billReminders;
    private boolean emailNotifications;

    public boolean isBudgetAlerts() {
        return budgetAlerts;
    }

    public void setBudgetAlerts(boolean budgetAlerts) {
        this.budgetAlerts = budgetAlerts;
    }

    public boolean isGoalReminders() {
        return goalReminders;
    }

    public void setGoalReminders(boolean goalReminders) {
        this.goalReminders = goalReminders;
    }

    public boolean isBillReminders() {
        return billReminders;
    }

    public void setBillReminders(boolean billReminders) {
        this.billReminders = billReminders;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
}
