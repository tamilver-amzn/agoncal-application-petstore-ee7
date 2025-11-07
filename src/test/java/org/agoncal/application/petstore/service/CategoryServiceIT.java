package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Category;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryServiceIT
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private CategoryService categoryservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(CategoryService.class)
            .addClass(Category.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assertions.assertNotNull(categoryservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = categoryservice.listAll().size();

      // Creates an object
      Category category = new Category();
      category.setName("Dummy value");
      category.setDescription("Dummy value");

      // Inserts the object into the database
      category = categoryservice.persist(category);
      assertNotNull(category.getId());
      assertEquals(initialSize + 1, categoryservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      category = categoryservice.findById(category.getId());
      assertEquals("Dummy value", category.getName());

      // Updates the object
      category.setName("A new value");
      category = categoryservice.merge(category);

      // Finds the object from the database and checks it has been updated
      category = categoryservice.findById(category.getId());
      assertEquals("A new value", category.getName());

      // Deletes the object from the database and checks it's not there anymore
      categoryservice.remove(category);
      assertEquals(initialSize, categoryservice.listAll().size());
   }
}
