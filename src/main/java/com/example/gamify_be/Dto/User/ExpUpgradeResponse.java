package com.example.gamify_be.Dto.User;

public class ExpUpgradeResponse {
    private boolean upgrade;

    public ExpUpgradeResponse(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }
}
