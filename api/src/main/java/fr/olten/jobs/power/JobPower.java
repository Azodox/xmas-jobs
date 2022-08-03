package fr.olten.jobs.power;

import lombok.Getter;

/**
 * @author Azodox_ (Luke)
 * 3/8/2022.
 */

public enum JobPower {

    MERCHANT_POWER(0);

    private @Getter final int powerId;

    JobPower(int powerId) {
        this.powerId = powerId;
    }

    public static JobPower getPower(int powerId){
        for(JobPower power : JobPower.values()){
            if(power.powerId == powerId){
                return power;
            }
        }
        return null;
    }
}

