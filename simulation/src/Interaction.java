import java.util.Random;

public class Interaction {


    public void performInteraction(Animal current, Animal enemy){
        if(betterSightThanCamouflage(current,enemy)){ // A sees B
            if(randomizedAction(current)){ // A wants to attack B
                if(betterSightThanCamouflage(enemy,current)){ // B sees A
                    if(randomizedAction(enemy)){ // B wants to attack A
                        // Both want to fight
                        double random = Math.random();
                        if(random<=0.5)
                            attack(current,enemy);
                        else
                            attack(enemy,current);
                    }else{
                        if(isFaster(current,enemy)) // B wants to run
                            attack(current, enemy); // A catch B
                        else
                            exhaust(current, enemy); // Both exhausted after chase
                    }
                }else{
                    // A does surprise attack on B
                    attack(current,enemy,Math.random()/10);
                }
            }else{ // A doesn't want to fight
                if(betterSightThanCamouflage(enemy,current)){ //B sees A
                    if(randomizedAction(enemy)){// B want to attack A
                        if(isFaster(enemy,current))
                            // B catch A
                            attack(enemy, current);
                        else
                            // Both exhausted after chase
                            exhaust(enemy, current);
                    }
                }
            }
        }else if(betterSightThanCamouflage(enemy,current) && randomizedAction(enemy)){ // B sees A and want to attack

            attack(enemy,current,Math.random()/10);
        }
    }

    private boolean betterSightThanCamouflage(Animal A, Animal B){
        return A.getGene(GenomCode.SIGHT) >= B.getGene(GenomCode.CAMOUFLAGE);
    }


    private boolean isFaster(Animal A, Animal B){
        return A.getGene(GenomCode.SPEED)>=B.getGene(GenomCode.SPEED);
    }

    private boolean randomizedAction(Animal animal){
        double random = Math.random();
        return random<=animal.aggresion;
    }

    private void attack(Animal A, Animal B){
        exhaust(A,B);
         if(A.getGene(GenomCode.STRENGTH) >= B.getGene(GenomCode.STRENGTH)){
             A.satiety += B.satiety;
             B.satiety = 0;
        }else{
             B.satiety += A.satiety;
             A.satiety = 0;
         }
    }

    private void attack(Animal A, Animal B, double surpriseModif){
        exhaust(A,B);
        if(A.getGene(GenomCode.STRENGTH)+surpriseModif >= B.getGene(GenomCode.STRENGTH)){
            A.satiety += B.satiety;
            B.satiety = 0;
        }else{
            B.satiety += A.satiety;
            A.satiety = 0;
        }
    }

    private void exhaust(Animal A, Animal B){
        double exhaustValue = Math.random()/10;
        A.satiety-=exhaustValue;
        B.satiety-=exhaustValue;
    }
}
