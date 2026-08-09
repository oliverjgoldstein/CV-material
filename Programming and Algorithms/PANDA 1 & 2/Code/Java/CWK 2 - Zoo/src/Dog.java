 public class Dog extends Animal {
    @Override
    void eat(Food food) {
    food.eaten(this);
  }
}
