public class Zoo 
{
  public static void main(String[] args) 
  {
    Zoo zoo = new Zoo();

    String arg = args[0];
    switch (arg) {
      case "-p1":
        zoo.test1();
        break;
      case "-p2":
        zoo.test2();
        break;
      case "-p3":
        zoo.test3();
        break;
      default:
        System.out.println("The argument: " + arg + " has not been recognised");
    }
  }

  public void feed(Animal animal, Food food) {
    animal.eat(food);
  }

  public void test1()
  {
    Animal animal = new Animal();
    Food food     = new Food();
    feed(animal, food);
  }

  public void test2()
  {
    Dog scooby = new Dog ();
    Food food  = new Food();
    feed(scooby, food);
  }

  public void test3()
  {
    Animal[] animals = new Animal[]
    { new Animal()
    , new Dog ()
    , new Cat ()
    };

    Food[] foods = new Food[]
    { new Food()
    , new Fruit()
    , new Chocolate()
    };

    for (Animal animal : animals)
    {
      for (Food food : foods)
      {
        feed(animal, food);
      }
    }
  }
}
