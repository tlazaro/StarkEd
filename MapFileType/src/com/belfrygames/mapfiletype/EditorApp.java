package com.belfrygames.mapfiletype;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;

/**
 *
 * @author tomas
 */
public class EditorApp implements ApplicationListener {

    private Color color;
    private Mesh mesh;
    
    private ApplicationListener listener;

    public EditorApp(Color color, ApplicationListener listener) {
        this.color = color;
        this.listener = listener;
    }

    @Override
    public void create() {
        if (mesh == null) {
            mesh = new Mesh(true, 3, 3,
                    new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

            mesh.setVertices(new float[]{-0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0, 0.5f, 0});
            mesh.setIndices(new short[]{0, 1, 2});
        }
        
        listener.create();
    }

    private int width = 1;
    private int height = 1;
    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        
        listener.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        mesh.render(GL10.GL_TRIANGLES, 0, 3);
        
        listener.render();
    }

    @Override
    public void pause() {
        listener.pause();
    }

    @Override
    public void resume() {
        listener.resume();
    }

    @Override
    public void dispose() {
        listener.dispose();
    }
}
