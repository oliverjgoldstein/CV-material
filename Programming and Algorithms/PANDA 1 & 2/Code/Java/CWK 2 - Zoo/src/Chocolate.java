public class Chocolate extends Food {
      void eaten(Animal animal) {
    System.out.println("animal eats chocolate");
  }
  // This uses method overloading.
  void eaten(Dog dog) {
    System.out.println("dog eats chocolate");
  }
  void eaten(Cat cat) {
    System.out.println("cat eats chocolate");
  }
}
