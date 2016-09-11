if(!npc.getScriptData().hasKey("quest_stage")){
    npc.getScriptData().setShort("quest_stage", 0);
    player.sendMessage("Hello " + player.getName() + "! Can you please get me a diamond?");
}else if(npc.getScriptData().getShort("quest_stage") == 0){
    if(!player.getInventory().isHeldNull() && itemstack.getItem().getUnlocalizedName() == "minecraft:diamond"){
        player.sendMessage("Thank you for the diamond!");
        npc.getScriptData().setShort("quest_stage", 1);
        itemstack.incStackSize(-1);
        if(itemstack.getStackSize() <= 0){
            player.getInventory().clearHeldItem();
        }
    }else{
         player.sendMessage("I am still waiting for my 1 diamond.");
    }
}else{
    player.sendMessage("Thanks again for the diamond!");
}

