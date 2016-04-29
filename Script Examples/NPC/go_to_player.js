if(npc.moveToBlock(player.getX(), player.getY(), player.getZ())){
    player.sendMessage("On my way!");
}else{
    player.sendMessage("I am not able to come to you at this moment!");
}
