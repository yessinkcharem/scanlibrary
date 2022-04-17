//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

class Loader {
  private static boolean done = false;

  Loader() {
  }

  protected static synchronized void load() {
    if (!done) {
      System.loadLibrary("Scanner");
      System.loadLibrary("opencv_java3");
      done = true;
    }
  }
}
