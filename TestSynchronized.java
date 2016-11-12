//测试同一个实例中不同的synchronized的方法是否会阻塞，答案是会，如fun和foo
//非synchronized的方法就不会被阻塞，查看fee
public class TestSynchronized {
	//同步方法
	synchronized void fun(){
		for(int i=0;i<10;i++)
			System.out.println("fun: "+i);
	}
	
	//另一个同步方法，在多线程时，会被阻塞，因为synchronized拿到的是该对象的锁
	synchronized void foo(){
		for(int i=0;i<10;i++)
			System.out.println("foo: "+i);
	}
	
	//非synchronized的方法，不会被阻塞
	void fee(){
		for(int i=0;i<10;i++)
			System.out.println("fee: "+i);
	}
	
	static public void main(String[] args){
		TestSynchronized testSynchronized = new TestSynchronized();
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				testSynchronized.fun();
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				testSynchronized.foo();
			}
		});
		Thread thread3 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				testSynchronized.fee();
			}
		});
		thread1.start();
		thread2.start();
		thread3.start();
	}
}
/* 
output:
fee: 0
fee: 1
fun: 0
fun: 1
fun: 2
fun: 3
fee: 2
fee: 3
fee: 4
fee: 5
fee: 6
fee: 7
fee: 8
fun: 4
fun: 5
fun: 6
fee: 9
fun: 7
fun: 8
fun: 9
foo: 0
foo: 1
foo: 2
foo: 3
foo: 4
foo: 5
foo: 6
foo: 7
foo: 8
foo: 9
 * */