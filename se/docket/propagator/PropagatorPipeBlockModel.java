package se.docket.propagator;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
 
public class PropagatorPipeBlockModel extends ModelBase
{
  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
 
  public PropagatorPipeBlockModel()
  {
    textureWidth = 32;
    textureHeight = 32;
   
      Shape1 = new ModelRenderer(this, 0, 4);
      Shape1.addBox(0F, 0F, 0F, 4, 4, 1);
      Shape1.setRotationPoint(-2F, 14F, 7F);
      Shape1.setTextureSize(32, 32);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 0, 0);
      Shape2.addBox(0F, 0F, 0F, 14, 2, 2);
      Shape2.setRotationPoint(-1F, 15F, 7F);
      Shape2.setTextureSize(32, 32);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 1.570796F, 0F);
      Shape3 = new ModelRenderer(this, 0, 4);
      Shape3.addBox(0F, 0F, 0F, 4, 4, 1);
      Shape3.setRotationPoint(-2F, 14F, -8F);
      Shape3.setTextureSize(32, 32);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 0, 4);
      Shape4.addBox(0F, 0F, 0F, 4, 4, 1);
      Shape4.setRotationPoint(-2F, 24F, -2F);
      Shape4.setTextureSize(32, 32);
      Shape4.mirror = true;
      setRotation(Shape4, 1.570796F, 0F, 0F);
      Shape5 = new ModelRenderer(this, -1, 0);
      Shape5.addBox(0F, 0F, 0F, 14, 2, 2);
      Shape5.setRotationPoint(1F, 9F, -1F);
      Shape5.setTextureSize(32, 32);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, 1.570796F);
      Shape6 = new ModelRenderer(this, 0, 4);
      Shape6.addBox(0F, 0F, 0F, 4, 4, 1);
      Shape6.setRotationPoint(-2F, 9F, -2F);
      Shape6.setTextureSize(32, 32);
      Shape6.mirror = true;
      setRotation(Shape6, 1.570796F, 0F, 0F);
      Shape7 = new ModelRenderer(this, 0, 4);
      Shape7.addBox(0F, 0F, 0F, 4, 4, 1);
      Shape7.setRotationPoint(7F, 14F, 2F);
      Shape7.setTextureSize(32, 32);
      Shape7.mirror = true;
      setRotation(Shape7, 0F, 1.570796F, 0F);
      Shape8 = new ModelRenderer(this, 0, 4);
      Shape8.addBox(0F, 0F, 0F, 4, 4, 1);
      Shape8.setRotationPoint(-8F, 14F, 2F);
      Shape8.setTextureSize(32, 32);
      Shape8.mirror = true;
      setRotation(Shape8, 0F, 1.570796F, 0F);
      Shape9 = new ModelRenderer(this, 0, 0);
      Shape9.addBox(0F, 0F, 0F, 14, 2, 2);
      Shape9.setRotationPoint(-7F, 15F, -1F);
      Shape9.setTextureSize(32, 32);
      Shape9.mirror = true;
      setRotation(Shape9, 0F, 0F, 0F);
  }
 
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape1.render(f5);
    Shape2.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
    Shape6.render(f5);
    Shape7.render(f5);
    Shape8.render(f5);
    Shape9.render(f5);
  }
 
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  @Override
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity par7Entity) {
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
	}
 
}