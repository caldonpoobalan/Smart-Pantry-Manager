package com.smartpantry.manager.database;

import com.smartpantry.manager.dao.RecpDataAcc;
import com.smartpantry.manager.dao.RecpIngredDataAcc;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;

import java.util.ArrayList;
import java.util.List;

// starter test recipes for database
public class RecpSeedData {

    // 3 simple recipes to test the matching logic
    public static void seedDefaultRecp(PantryRoomDb appDb) {
        RecpDataAcc recpDao = appDb.recpDataAcc();
        RecpIngredDataAcc ingDao = appDb.recpIngredDataAcc();

        // 1. Baked Tomato and Cheese
        long r1 = recpDao.insertRecp(new RecpEntity(
                "Baked Tomato and Cheese",
                "Warm baked tomato halves topped with bubbling melted cheese",
                "1. Preheat oven to 200°C (400°F).\n2. Place tomato halves cut-side up on a tray.\n3. Sprinkle with salt and black pepper.\n4. Top each half with shredded cheese.\n5. Bake for 10-12 minutes until cheese bubbles."));

        List<RecpIngredEntity> i1 = new ArrayList<>();
        i1.add(new RecpIngredEntity((int) r1, "tomato", 2, "pcs"));
        i1.add(new RecpIngredEntity((int) r1, "cheese", 50, "g"));
        i1.add(new RecpIngredEntity((int) r1, "salt", 1, "g"));
        i1.add(new RecpIngredEntity((int) r1, "black pepper", 1, "g"));
        ingDao.insertAllIngred(i1);

        // 2. Cream Potato Fries
        long r2 = recpDao.insertRecp(new RecpEntity(
                "Cream Potato Fries",
                "Crispy golden oven-baked potato wedges tossed in cream",
                "1. Preheat oven to 200°C (400°F).\n2. Cut potatoes into thin wedges.\n3. Toss wedges in a bowl with cream, salt, and black pepper.\n4. Spread on a baking sheet.\n5. Bake for 25-30 minutes until golden."));

        List<RecpIngredEntity> i2 = new ArrayList<>();
        i2.add(new RecpIngredEntity((int) r2, "potato", 2, "pcs"));
        i2.add(new RecpIngredEntity((int) r2, "cream", 45, "ml"));
        i2.add(new RecpIngredEntity((int) r2, "salt", 1, "g"));
        i2.add(new RecpIngredEntity((int) r2, "black pepper", 1, "g"));
        ingDao.insertAllIngred(i2);

        // 3. Devil Eggs
        long r3 = recpDao.insertRecp(new RecpEntity(
                "Devil Eggs",
                "Creamy whipped yolk stuffed hard-boiled eggs",
                "1. Peel hard-boiled eggs and cut in half lengthwise.\n2. Remove yellow yolks and place in a bowl.\n3. Add cream, salt, and black pepper to the yolks.\n4. Mash with a fork until smooth.\n5. Spoon mixture back into the whites."));

        List<RecpIngredEntity> i3 = new ArrayList<>();
        i3.add(new RecpIngredEntity((int) r3, "egg", 3, "pcs"));
        i3.add(new RecpIngredEntity((int) r3, "cream", 15, "ml"));
        i3.add(new RecpIngredEntity((int) r3, "salt", 1, "g"));
        i3.add(new RecpIngredEntity((int) r3, "black pepper", 1, "g"));
        ingDao.insertAllIngred(i3);
    }
}
