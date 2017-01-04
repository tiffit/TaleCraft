if(!npc.getScriptData().hasKey("quest_stage")){
    var dialogue_ask = npc.createDialogue("ask");
    dialogue_ask.setTitle("Diamond Quest");
    dialogue_ask.setText("Hey, could you get me a diamond please?");
    dialogue_ask.addOption("Sure!", null, "action.nbt={quest_stage:0s}");
    dialogue_ask.addOption("No, sorry not now.");
    npc.sendDialogue("ask", player);
}else if(npc.getScriptData().getShort("quest_stage") == 0){
    if(!player.getInventory().isHeldNull() && itemstack.getItem().getUnlocalizedName() == "item.diamond"){
        var dialogue_thank = npc.createDialogue("thank");
        dialogue_thank.setTitle("Diamond Quest");
        dialogue_thank.setText("Thanks for the diamond!");
        dialogue_thank.addOption("No problem!");
        npc.sendDialogue("thank", player);
        npc.getScriptData().setShort("quest_stage", 1);
        itemstack.incStackSize(-1);
        if(itemstack.getStackSize() <= 0){
            player.getInventory().clearHeldItem();
        }
    }else{
         var dialogue_wait = npc.createDialogue("wait");
         dialogue_wait.setTitle("Diamond Quest");
         dialogue_wait.setText("Still waiting for my 1 diamond!");
         dialogue_wait.addOption("On it!");
         npc.sendDialogue("wait", player);
    }
}else{
    var dialogue_thankagain = npc.createDialogue("thankagain");
    dialogue_thankagain.setTitle("Diamond Quest");
    dialogue_thankagain.setText("Thanks again for the diamond!");
    dialogue_thankagain.addOption("No problem!");
    npc.sendDialogue("thankagain", player);
}

