public class Food {
  void eaten(Animal animal) {
    System.out.println("animal eats food");
  }
  // This uses method overloading.
  void eaten(Dog dog) {
    System.out.println("dog eats food");
  }
  void eaten(Cat cat) {
    System.out.println("cat eats food");
  }
}
