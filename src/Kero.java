import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Kero {

	double deltaX,deltaY;
	double r,g,b;
	boolean dying;

	public Kero()
	{
		this(0,0,0,0,0);
	}


	public Kero(double x,double y,double r,double g,double b)
	{
		this.deltaX=x;
		this.deltaY=y;
		this.r=r;
		this.g=g;
		this.b=b;
		this.dying = false;
	}
	public void ChangeDeltaX(boolean positive)
	{
		if(positive)
		{
			if(this.deltaX>0)this.deltaX*= 1;
			else this.deltaX*= -1;
		}
		else
		{
			if(this.deltaX>0)this.deltaX*= -1;
			else this.deltaX*= 1;
		}
	}

	public void ChangeDeltaY(boolean positive)
	{
		if(positive)
		{
			if(this.deltaY>0)this.deltaY*= 1;
			else this.deltaY*= -1;
		}
		else
		{
			if(this.deltaY>0)this.deltaY*= -1;
			else this.deltaY*= 1;
		}
	}


	public static Polygon createFish(double x, double y,double r,double g,double b,boolean rightD)
	{
		Polygon pol = new Polygon();
		if(rightD)
//			Baby shark
			pol.getPoints().addAll(new Double[]{
					-12.0,0.0,
					-5.0,2.0,
					-23.0,4.0,
					-29.0,9.0,
					-27.0,4.0,
					-64.0,2.0,
					-71.0,5.0,
					-69.0,0.0,
					-77.0,-8.0,
					-70.0,-6.0,
					-64.0,-2.0,
					-40.0,-10.0,
					-40.0,-19.0,
					-30.0,-10.0,
					-12.0,-6.0,
					0.0,0.0});

		else
			pol.getPoints().addAll(new Double[]{-77.0,0.0,
					-65.0,-6.0,
					-47.0,-10.0,
					-37.0,-19.0,
					-37.0,-10.0,
					-13.0,-2.0,
					-7.0,-6.0,
					-0.0,-8.0,
					-8.0,-0.0,
					-6.0,5.0,
					-13.0,2.0,
					-50.0,4.0,
					-48.0,9.0,
					-54.0,4.0,
					-72.0,2.0,
					-60.0,0.0});
		pol.setFill(Color.color(r, g, b));
		pol.setLayoutX(x);
		pol.setLayoutY(y);
		return pol;
	}	
}
