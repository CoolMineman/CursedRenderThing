package org.jglrxavpok.glutils;

//import cpw.mods.fml.common.eventhandler.EventBus;
//import net.minecraft.client.renderer.Tessellator;

import org.jglrxavpok.glutils.TessellatorModelEvent.RenderGroupEvent;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.Tessellator;

/**
 * This class loads and renders a mesh from an OBJ file format. The mesh can
 * have multiple materials, including texture images.
 * 
 * Uses GL_Mesh to load a .obj file and GLMaterialLIb to load the .mtl file. It
 * assumes the .obj .mtl and any texture images are present in the same folder.
 * 
 * Also has a function, renderTextured() to draw a mesh with no groups or
 * materials. The entire mesh will be drawn as one group of triangles with one
 * texture.
 */
public class TessellatorModel extends GLModel
{

    //public static final EventBus MODEL_RENDERING_BUS = new EventBus();
    
    public TessellatorModel(String filename)
    {
        super(filename);
    }

    /**
     * Draw the model. Calls the displaylist if one is created, or calls
     * renderGroups()
     */
    public void render()
    {
        // if (displayListID == 0)
        // {
        //if(!MODEL_RENDERING_BUS.post(new TessellatorModelEvent.RenderPre(
        //        this)))
            render(mesh);
        // } else
        // {
        // GL11.glCallList(displayListID);
        // }
    }

    /**
     * Draw one group from the mesh. This will activate the correct material for
     * the group (including textures).
     * 
     * @param groupName
     *            name of group (from obj file)
     */
    public void renderGroup(String groupName)
    {
        //if(!MODEL_RENDERING_BUS.post(new RenderGroupEvent.Pre(groupName,
        //        this)))
        //{
            int GID = -1; // group id

            // find group by name
            for (int g = 0; g < mesh.numGroups(); g++)
            {
                if(mesh.getGroupName(g).equals(groupName))
                {
                    GID = g;
                    break;
                }
            }
            if(GID == -1)
            {
                return;
            }

            // draw the triangles in this group
            GLMaterial[] materials = mesh.materials; // loaded from the .mtl
                                                     // file
            GL_Triangle[] triangles = mesh.getGroupFaces(GID); // each group may
                                                               // have a
                                                               // material
            GLMaterial mtl;
            GL_Triangle t;
            int currMtl = -1;
            int i = 0;

            // draw all triangles in object
            if(triangles!=null) {
                for (i = 0; i < triangles.length;)
                {
                    t = triangles[i];
                    
                    // draw triangles until material changes
                    Tessellator tess = Tessellator.INSTANCE;
                    tess.start(GL11.GL_TRIANGLES);

                    // activate new material and texture
                    currMtl = t.materialID;
                    mtl = (materials != null && materials.length > 0 && currMtl >= 0) ? materials[currMtl] : defaultMtl;
                    mtl.apply();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, mtl.textureHandle);

                    for (; i < triangles.length && (t = triangles[i]) != null && currMtl == t.materialID; i++) {
                        tess.method_1697(t.norm1.x, t.norm1.y, t.norm1.z); //setNormal
                        tess.size(t.uvw1.x, t.uvw1.y); //setTextureUV
                        tess.pos((float) t.p1.pos.x, (float) t.p1.pos.y, (float) t.p1.pos.z); //addVertex

                        tess.size(t.uvw2.x, t.uvw2.y);
                        tess.method_1697(t.norm2.x, t.norm2.y, t.norm2.z);
                        tess.pos((float) t.p2.pos.x, (float) t.p2.pos.y, (float) t.p2.pos.z);

                        tess.size(t.uvw3.x, t.uvw3.y);
                        tess.method_1697(t.norm3.x, t.norm3.y, t.norm3.z);
                        tess.pos((float) t.p3.pos.x, (float) t.p3.pos.y, (float) t.p3.pos.z);
                    }
                    tess.draw();
                }
            }

        // MODEL_RENDERING_BUS.post(new RenderGroupEvent.Post(groupName,
        // this));
        // }
    }

    /**
     * This is a simple way to render a mesh with no materials. Draws the mesh with
     * normals and texture coordinates. Loops through all triangles in the mesh
     * object (ignores groups and materials).
     * 
     * @param o mesh object to render
     */
    public void renderTextured(int textureHandle) {
        GL_Triangle t;
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        tess.start(GL11.GL_TRIANGLES);
        for (int j = 0; j < mesh.triangles.length; j++) { // draw all triangles in object
            t = mesh.triangles[j];

            tess.size(t.uvw1.x, t.uvw1.y);
            tess.method_1697(t.norm1.x, t.norm1.y, t.norm1.z);
            tess.pos((float) t.p1.pos.x, (float) t.p1.pos.y, (float) t.p1.pos.z);

            tess.size(t.uvw2.x, t.uvw2.y);
            tess.method_1697(t.norm2.x, t.norm2.y, t.norm2.z);
            tess.pos((float) t.p2.pos.x, (float) t.p2.pos.y, (float) t.p2.pos.z);

            tess.size(t.uvw3.x, t.uvw3.y);
            tess.method_1697(t.norm3.x, t.norm3.y, t.norm3.z);
            tess.pos((float) t.p3.pos.x, (float) t.p3.pos.y, (float) t.p3.pos.z);
        }
        tess.draw();
    }

    /**
     * Render a mesh with materials. If no materials exist (none are defined in the
     * mesh, or the materials file was not found), then a default material will be
     * applied and texture 0 will be activated (see GLMaterial.java for the default
     * material settings).
     */
    public void render(GL_Mesh m) {
        int i = 0;

        for (; i < m.groupNames.length; i++) {
            renderGroup(m.groupNames[i]);
        }
    }

    public void renderMeshNormals() {
        GL_Triangle t;
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(0, 1, 0);
        Tessellator tess = Tessellator.INSTANCE;
        tess.start(GL11.GL_LINES);
        {
            for (int j = 0; j < mesh.triangles.length; j++)
            { // draw all triangles in object
                t = mesh.triangles[j];
                t.norm1.normalize();
                t.norm2.normalize();
                t.norm3.normalize();

                tess.pos((float) t.p1.pos.x, (float) t.p1.pos.y,
                        (float) t.p1.pos.z);
                tess.pos((float) (t.p1.pos.x + t.norm1.x),
                        (float) (t.p1.pos.y + t.norm1.y),
                        (float) (t.p1.pos.z + t.norm1.z));

                tess.pos((float) t.p2.pos.x, (float) t.p2.pos.y,
                        (float) t.p2.pos.z);
                tess.pos((float) (t.p2.pos.x + t.norm2.x),
                        (float) (t.p2.pos.y + t.norm2.y),
                        (float) (t.p2.pos.z + t.norm2.z));

                tess.pos((float) t.p3.pos.x, (float) t.p3.pos.y,
                        (float) t.p3.pos.z);
                tess.pos((float) (t.p3.pos.x + t.norm3.x),
                        (float) (t.p3.pos.y + t.norm3.y),
                        (float) (t.p3.pos.z + t.norm3.z));
            }
        }
        tess.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
