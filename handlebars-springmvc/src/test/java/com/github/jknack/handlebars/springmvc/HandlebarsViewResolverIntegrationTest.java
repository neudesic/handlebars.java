package com.github.jknack.handlebars.springmvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.View;

import com.github.jknack.handlebars.Handlebars;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HandlebarsApp.class })
public class HandlebarsViewResolverIntegrationTest {

  @Autowired
  HandlebarsViewResolver viewResolver;

  @Test
  public void getHandlebars() throws Exception {
    assertNotNull(viewResolver);
    assertNotNull(viewResolver.getHandlebars());
  }

  @Test
  public void resolveView() throws Exception {
    assertNotNull(viewResolver);
    View view = viewResolver.resolveViewName("template", Locale.getDefault());
    assertNotNull(view);
    assertEquals(HandlebarsView.class, view.getClass());
  }

  @Test
  public void resolveViewWithFallback() throws Exception {
    try {
      assertNotNull(viewResolver);
      viewResolver.setFailOnMissingFile(false);
      View view = viewResolver.resolveViewName("invalidView", Locale.getDefault());
      assertNull(view);
    } finally {
      viewResolver.setFailOnMissingFile(true);
    }
  }

  @Test(expected = IOException.class)
  public void failToResolve() throws Exception {
    try {
      assertNotNull(viewResolver);
      viewResolver.setFailOnMissingFile(true);
      viewResolver.resolveViewName("invalidView", Locale.getDefault());
    } finally {
      viewResolver.setFailOnMissingFile(true);
    }
  }

  @Test(expected = IllegalStateException.class)
  public void getHandlebarsFail() throws Exception {
    assertNotNull(new HandlebarsViewResolver().getHandlebars());
  }

  @Test
  public void messageHelper() throws Exception {
    assertNotNull(viewResolver);
    Handlebars handlebars = viewResolver.getHandlebars();
    assertEquals("Handlebars Spring MVC!", handlebars.compileInline("{{message \"hello\"}}").apply(new Object()));
  }


  @Test
  public void messageHelperWithParams() throws Exception {
    assertNotNull(viewResolver);
    Handlebars handlebars = viewResolver.getHandlebars();
    assertEquals("Hello Handlebars!", handlebars.compileInline("{{message \"hello.0\" \"Handlebars\"}}").apply(new Object()));
    assertEquals("Hello Spring MVC!", handlebars.compileInline("{{message \"hello.0\" \"Spring MVC\"}}").apply(new Object()));
  }

  @Test
  public void messageHelperWithDefaultValue() throws Exception {
    assertNotNull(viewResolver);
    Handlebars handlebars = viewResolver.getHandlebars();
    assertEquals("hey", handlebars.compileInline("{{message \"hi\" default='hey'}}").apply(new Object()));
  }

  @Test
  public void customHelper() throws Exception {
    assertNotNull(viewResolver);
    Handlebars handlebars = viewResolver.getHandlebars();
    assertEquals("Spring Helper", handlebars.compileInline("{{spring}}").apply(new Object()));
  }

  @Test
  public void setCustomHelper() throws Exception {
    assertNotNull(viewResolver);
    Handlebars handlebars = viewResolver.getHandlebars();
    assertEquals("Spring Helper", handlebars.compileInline("{{setHelper}}").apply(new Object()));
  }

  @Test
  public void helperSource() throws Exception {
    assertNotNull(viewResolver);
    Handlebars handlebars = viewResolver.getHandlebars();
    assertEquals("helper source!", handlebars.compileInline("{{helperSource}}").apply(new Object()));
  }
}
