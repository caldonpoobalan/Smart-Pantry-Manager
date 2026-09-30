package com.smartpantry.manager.database;

import com.smartpantry.manager.dao.RecpDataAcc;
import com.smartpantry.manager.dao.RecpIngredDataAcc;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// starter test recipes for database
public class RecpSeedData {

    // ensures all starter recipes are populated in the database if missing
    public static void ensureSeedData(PantryRoomDb appDb) {
        RecpDataAcc recpDao = appDb.recpDataAcc();
        List<RecpEntity> existing = recpDao.getAllRecp();
        if (existing == null || existing.isEmpty()) {
            seedDefaultRecp(appDb);
        } else if (existing.size() < 18) {
            seedAdditionalRecp(appDb, existing);
        }
    }

    // populates all 18 diverse starter recipes
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

        // populates remaining 15 recipes
        seedAdditionalRecp(appDb, recpDao.getAllRecp());
    }

    // populates additional recipes to expand database to 18 diverse options
    private static void seedAdditionalRecp(PantryRoomDb appDb, List<RecpEntity> existingList) {
        RecpDataAcc recpDao = appDb.recpDataAcc();
        RecpIngredDataAcc ingDao = appDb.recpIngredDataAcc();

        Set<String> existingNames = new HashSet<>();
        if (existingList != null) {
            for (RecpEntity r : existingList) {
                existingNames.add(r.getRecpNm().trim().toLowerCase());
            }
        }

        // 4. Classic Scrambled Eggs
        if (!existingNames.contains("classic scrambled eggs")) {
            long r4 = recpDao.insertRecp(new RecpEntity(
                    "Classic Scrambled Eggs",
                    "Soft and creamy scrambled eggs cooked gently in melted butter",
                    "1. Whisk eggs and milk in a bowl with a pinch of salt and black pepper.\n2. Melt butter in a non-stick pan over medium-low heat.\n3. Pour in whisked eggs and stir gently with a spatula.\n4. Remove from heat while still slightly soft and glossy."));
            List<RecpIngredEntity> i4 = new ArrayList<>();
            i4.add(new RecpIngredEntity((int) r4, "egg", 2, "pcs"));
            i4.add(new RecpIngredEntity((int) r4, "butter", 15, "g"));
            i4.add(new RecpIngredEntity((int) r4, "milk", 30, "ml"));
            i4.add(new RecpIngredEntity((int) r4, "salt", 1, "g"));
            i4.add(new RecpIngredEntity((int) r4, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i4);
        }

        // 5. Garlic Butter Pasta
        if (!existingNames.contains("garlic butter pasta")) {
            long r5 = recpDao.insertRecp(new RecpEntity(
                    "Garlic Butter Pasta",
                    "Tender pasta tossed in fragrant golden garlic and rich melted butter",
                    "1. Boil pasta in salted water until tender, then drain.\n2. Melt butter in a skillet over medium heat.\n3. Add minced garlic and cook for 1 minute until fragrant.\n4. Toss pasta into the garlic butter with black pepper and serve warm."));
            List<RecpIngredEntity> i5 = new ArrayList<>();
            i5.add(new RecpIngredEntity((int) r5, "pasta", 150, "g"));
            i5.add(new RecpIngredEntity((int) r5, "butter", 30, "g"));
            i5.add(new RecpIngredEntity((int) r5, "garlic", 2, "pcs"));
            i5.add(new RecpIngredEntity((int) r5, "salt", 1, "g"));
            i5.add(new RecpIngredEntity((int) r5, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i5);
        }

        // 6. Cheesy Garlic Bread
        if (!existingNames.contains("cheesy garlic bread")) {
            long r6 = recpDao.insertRecp(new RecpEntity(
                    "Cheesy Garlic Bread",
                    "Crispy toasted bread slices topped with savory garlic butter and melted cheese",
                    "1. Preheat oven to 190°C (375°F).\n2. Mix softened butter with minced garlic in a small dish.\n3. Spread garlic butter evenly over bread slices.\n4. Top with shredded cheese and bake for 8-10 minutes until bubbly and golden."));
            List<RecpIngredEntity> i6 = new ArrayList<>();
            i6.add(new RecpIngredEntity((int) r6, "bread", 2, "pcs"));
            i6.add(new RecpIngredEntity((int) r6, "butter", 20, "g"));
            i6.add(new RecpIngredEntity((int) r6, "garlic", 1, "pcs"));
            i6.add(new RecpIngredEntity((int) r6, "cheese", 40, "g"));
            ingDao.insertAllIngred(i6);
        }

        // 7. Tomato Egg Stir Fry
        if (!existingNames.contains("tomato egg stir fry")) {
            long r7 = recpDao.insertRecp(new RecpEntity(
                    "Tomato Egg Stir Fry",
                    "Homestyle stir-fried eggs folded with sweet and juicy ripe tomatoes",
                    "1. Scramble eggs lightly in a hot oiled pan and set aside.\n2. Cut tomatoes into wedges and cook in the skillet until soft and juicy.\n3. Stir in a pinch of sugar and salt.\n4. Fold the cooked scrambled eggs back into the tomatoes and stir for 1 minute."));
            List<RecpIngredEntity> i7 = new ArrayList<>();
            i7.add(new RecpIngredEntity((int) r7, "egg", 3, "pcs"));
            i7.add(new RecpIngredEntity((int) r7, "tomato", 2, "pcs"));
            i7.add(new RecpIngredEntity((int) r7, "cooking oil", 15, "ml"));
            i7.add(new RecpIngredEntity((int) r7, "salt", 1, "g"));
            i7.add(new RecpIngredEntity((int) r7, "sugar", 1, "g"));
            i7.add(new RecpIngredEntity((int) r7, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i7);
        }

        // 8. Simple Fried Rice
        if (!existingNames.contains("simple fried rice")) {
            long r8 = recpDao.insertRecp(new RecpEntity(
                    "Simple Fried Rice",
                    "Fragrant wok-style fried rice tossed with scrambled egg and savory soy sauce",
                    "1. Heat cooking oil in a large skillet over medium-high heat.\n2. Sauté chopped onion until soft and translucent.\n3. Push onion aside, crack in eggs, and scramble until set.\n4. Add cooked rice, drizzle with soy sauce, and stir-fry for 3 minutes."));
            List<RecpIngredEntity> i8 = new ArrayList<>();
            i8.add(new RecpIngredEntity((int) r8, "rice", 200, "g"));
            i8.add(new RecpIngredEntity((int) r8, "egg", 2, "pcs"));
            i8.add(new RecpIngredEntity((int) r8, "onion", 1, "pcs"));
            i8.add(new RecpIngredEntity((int) r8, "cooking oil", 15, "ml"));
            i8.add(new RecpIngredEntity((int) r8, "soy sauce", 15, "ml"));
            ingDao.insertAllIngred(i8);
        }

        // 9. Crispy Potato Wedges
        if (!existingNames.contains("crispy potato wedges")) {
            long r9 = recpDao.insertRecp(new RecpEntity(
                    "Crispy Potato Wedges",
                    "Golden oven-roasted potato wedges seasoned with minced garlic and pepper",
                    "1. Preheat oven to 210°C (410°F).\n2. Cut potatoes into wedges, soak in water for 10 minutes, and pat dry.\n3. Toss wedges with cooking oil, minced garlic, salt, and black pepper.\n4. Spread evenly on a baking sheet and bake for 30 minutes until crispy."));
            List<RecpIngredEntity> i9 = new ArrayList<>();
            i9.add(new RecpIngredEntity((int) r9, "potato", 3, "pcs"));
            i9.add(new RecpIngredEntity((int) r9, "cooking oil", 20, "ml"));
            i9.add(new RecpIngredEntity((int) r9, "garlic", 2, "pcs"));
            i9.add(new RecpIngredEntity((int) r9, "salt", 1, "g"));
            i9.add(new RecpIngredEntity((int) r9, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i9);
        }

        // 10. Creamy Tomato Soup
        if (!existingNames.contains("creamy tomato soup")) {
            long r10 = recpDao.insertRecp(new RecpEntity(
                    "Creamy Tomato Soup",
                    "Smooth comforting tomato soup simmered with onions and finished with rich cream",
                    "1. Sauté chopped onions in melted butter until translucent.\n2. Add chopped tomatoes and water, then simmer on medium heat for 15 minutes.\n3. Puree or mash soup until smooth, then stir in cream, salt, and black pepper.\n4. Warm through on low heat and serve with bread."));
            List<RecpIngredEntity> i10 = new ArrayList<>();
            i10.add(new RecpIngredEntity((int) r10, "tomato", 4, "pcs"));
            i10.add(new RecpIngredEntity((int) r10, "onion", 1, "pcs"));
            i10.add(new RecpIngredEntity((int) r10, "cream", 60, "ml"));
            i10.add(new RecpIngredEntity((int) r10, "butter", 20, "g"));
            i10.add(new RecpIngredEntity((int) r10, "water", 200, "ml"));
            i10.add(new RecpIngredEntity((int) r10, "salt", 1, "g"));
            i10.add(new RecpIngredEntity((int) r10, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i10);
        }

        // 11. Onion Omelette
        if (!existingNames.contains("onion omelette")) {
            long r11 = recpDao.insertRecp(new RecpEntity(
                    "Onion Omelette",
                    "Fluffy skillet omelette folded over sweet caramelized onions",
                    "1. Slice onion thinly.\n2. Heat cooking oil in a pan and cook onions over medium heat until tender and golden.\n3. Whisk eggs with salt and black pepper, then pour over the onions.\n4. Cook until edges are set, fold in half, and slide onto a plate."));
            List<RecpIngredEntity> i11 = new ArrayList<>();
            i11.add(new RecpIngredEntity((int) r11, "egg", 2, "pcs"));
            i11.add(new RecpIngredEntity((int) r11, "onion", 1, "pcs"));
            i11.add(new RecpIngredEntity((int) r11, "cooking oil", 10, "ml"));
            i11.add(new RecpIngredEntity((int) r11, "salt", 1, "g"));
            i11.add(new RecpIngredEntity((int) r11, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i11);
        }

        // 12. Garlic Chicken Skillet
        if (!existingNames.contains("garlic chicken skillet")) {
            long r12 = recpDao.insertRecp(new RecpEntity(
                    "Garlic Chicken Skillet",
                    "Juicy pan-seared chicken bites glazed in roasted garlic butter",
                    "1. Cut chicken into bite-sized chunks and season with salt and black pepper.\n2. Heat cooking oil and butter in a skillet over medium-high heat.\n3. Sear chicken pieces for 6-8 minutes until golden and thoroughly cooked.\n4. Add minced garlic in the final 2 minutes and toss to glaze evenly."));
            List<RecpIngredEntity> i12 = new ArrayList<>();
            i12.add(new RecpIngredEntity((int) r12, "chicken", 250, "g"));
            i12.add(new RecpIngredEntity((int) r12, "garlic", 3, "pcs"));
            i12.add(new RecpIngredEntity((int) r12, "butter", 25, "g"));
            i12.add(new RecpIngredEntity((int) r12, "cooking oil", 10, "ml"));
            i12.add(new RecpIngredEntity((int) r12, "salt", 1, "g"));
            i12.add(new RecpIngredEntity((int) r12, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i12);
        }

        // 13. Classic French Toast
        if (!existingNames.contains("classic french toast")) {
            long r13 = recpDao.insertRecp(new RecpEntity(
                    "Classic French Toast",
                    "Thick golden bread slices dipped in sweet milk custard and pan-fried in butter",
                    "1. Whisk eggs, milk, and sugar together in a shallow dish.\n2. Dip bread slices into the mixture for 15 seconds per side until soaked.\n3. Melt butter in a skillet over medium heat.\n4. Fry bread slices for 2-3 minutes per side until golden brown and fragrant."));
            List<RecpIngredEntity> i13 = new ArrayList<>();
            i13.add(new RecpIngredEntity((int) r13, "bread", 3, "pcs"));
            i13.add(new RecpIngredEntity((int) r13, "egg", 2, "pcs"));
            i13.add(new RecpIngredEntity((int) r13, "milk", 50, "ml"));
            i13.add(new RecpIngredEntity((int) r13, "butter", 15, "g"));
            i13.add(new RecpIngredEntity((int) r13, "sugar", 5, "g"));
            ingDao.insertAllIngred(i13);
        }

        // 14. Macaroni and Cheese
        if (!existingNames.contains("macaroni and cheese")) {
            long r14 = recpDao.insertRecp(new RecpEntity(
                    "Macaroni and Cheese",
                    "Comforting stovetop pasta coated in a rich and creamy homemade cheese sauce",
                    "1. Cook pasta in boiling salted water until tender, then drain.\n2. Melt butter in a saucepan and stir in milk over medium heat.\n3. Add shredded cheese gradually, stirring until melted into a smooth sauce.\n4. Stir in warm pasta and season with black pepper."));
            List<RecpIngredEntity> i14 = new ArrayList<>();
            i14.add(new RecpIngredEntity((int) r14, "pasta", 150, "g"));
            i14.add(new RecpIngredEntity((int) r14, "cheese", 80, "g"));
            i14.add(new RecpIngredEntity((int) r14, "milk", 100, "ml"));
            i14.add(new RecpIngredEntity((int) r14, "butter", 25, "g"));
            i14.add(new RecpIngredEntity((int) r14, "salt", 1, "g"));
            i14.add(new RecpIngredEntity((int) r14, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i14);
        }

        // 15. Pan-Seared Chicken and Potatoes
        if (!existingNames.contains("pan-seared chicken and potatoes")) {
            long r15 = recpDao.insertRecp(new RecpEntity(
                    "Pan-Seared Chicken and Potatoes",
                    "Hearty skillet meal of tender chicken cubes and crispy seasoned potatoes",
                    "1. Dice potatoes and chicken into bite-sized cubes, season with salt and black pepper.\n2. Heat cooking oil in a skillet and cook potatoes for 10 minutes until tender.\n3. Add chicken pieces and cook for 7 minutes until seared through.\n4. Stir in minced garlic and toss everything together for 2 minutes."));
            List<RecpIngredEntity> i15 = new ArrayList<>();
            i15.add(new RecpIngredEntity((int) r15, "chicken", 300, "g"));
            i15.add(new RecpIngredEntity((int) r15, "potato", 2, "pcs"));
            i15.add(new RecpIngredEntity((int) r15, "cooking oil", 20, "ml"));
            i15.add(new RecpIngredEntity((int) r15, "garlic", 2, "pcs"));
            i15.add(new RecpIngredEntity((int) r15, "salt", 1, "g"));
            i15.add(new RecpIngredEntity((int) r15, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i15);
        }

        // 16. Egg Salad Sandwich
        if (!existingNames.contains("egg salad sandwich")) {
            long r16 = recpDao.insertRecp(new RecpEntity(
                    "Egg Salad Sandwich",
                    "Creamy chopped egg salad spread thickly between soft sandwich bread",
                    "1. Peel hard-boiled eggs and chop finely into a small mixing bowl.\n2. Add cream, salt, and black pepper, then mash gently with a fork.\n3. Spread egg mixture generously between two slices of fresh bread.\n4. Cut sandwich in half and serve."));
            List<RecpIngredEntity> i16 = new ArrayList<>();
            i16.add(new RecpIngredEntity((int) r16, "egg", 2, "pcs"));
            i16.add(new RecpIngredEntity((int) r16, "bread", 2, "pcs"));
            i16.add(new RecpIngredEntity((int) r16, "cream", 20, "ml"));
            i16.add(new RecpIngredEntity((int) r16, "salt", 1, "g"));
            i16.add(new RecpIngredEntity((int) r16, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i16);
        }

        // 17. Sautéed Garlic Potatoes
        if (!existingNames.contains("sautéed garlic potatoes") && !existingNames.contains("sauteed garlic potatoes")) {
            long r17 = recpDao.insertRecp(new RecpEntity(
                    "Sauteed Garlic Potatoes",
                    "Crispy pan-fried potato cubes tossed in sizzling butter and crushed garlic",
                    "1. Cut potatoes into cubes and boil in water for 5 minutes, then drain.\n2. Melt butter in a wide frying pan over medium-high heat.\n3. Add potatoes and fry without disturbing for 5 minutes until bottom is golden.\n4. Stir in minced garlic, salt, and black pepper, tossing until fragrant."));
            List<RecpIngredEntity> i17 = new ArrayList<>();
            i17.add(new RecpIngredEntity((int) r17, "potato", 2, "pcs"));
            i17.add(new RecpIngredEntity((int) r17, "butter", 20, "g"));
            i17.add(new RecpIngredEntity((int) r17, "garlic", 2, "pcs"));
            i17.add(new RecpIngredEntity((int) r17, "salt", 1, "g"));
            i17.add(new RecpIngredEntity((int) r17, "black pepper", 1, "g"));
            ingDao.insertAllIngred(i17);
        }

        // 18. Chicken Fried Rice
        if (!existingNames.contains("chicken fried rice")) {
            long r18 = recpDao.insertRecp(new RecpEntity(
                    "Chicken Fried Rice",
                    "Savory pan-fried rice loaded with tender chicken pieces, scrambled egg, and onions",
                    "1. Cut chicken into small strips and cook in a hot oiled skillet until done.\n2. Add diced onions and cook for 2 minutes until softened.\n3. Push ingredients to the side, scramble the egg in the pan, then stir in cooked rice.\n4. Drizzle with soy sauce and toss on high heat for 3 minutes."));
            List<RecpIngredEntity> i18 = new ArrayList<>();
            i18.add(new RecpIngredEntity((int) r18, "chicken", 150, "g"));
            i18.add(new RecpIngredEntity((int) r18, "rice", 200, "g"));
            i18.add(new RecpIngredEntity((int) r18, "egg", 1, "pcs"));
            i18.add(new RecpIngredEntity((int) r18, "onion", 1, "pcs"));
            i18.add(new RecpIngredEntity((int) r18, "cooking oil", 15, "ml"));
            i18.add(new RecpIngredEntity((int) r18, "soy sauce", 15, "ml"));
            ingDao.insertAllIngred(i18);
        }
    }
}
