package com.design.mode.factory;
class BananaFactory implements FriutFactoryForSimple{
    public void makeFriut() {
        new Banana().get();
    }
   
}