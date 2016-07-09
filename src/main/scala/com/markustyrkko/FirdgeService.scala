package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import java.io.File
import java.io.FileOutputStream
import com.markustyrkko.FridgeBrain.Brain
import com.markustyrkko.FridgeBrain.Drink

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class FridgeServiceActor extends Actor with FridgeService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(fileRoute)
}


// this trait defines our service behavior independently from the service actor
trait FridgeService extends HttpService {

  val fileRoute = {
  	 post {
	    entity(as[MultipartFormData]) {
	      formData => {
	      	complete(Brain.analyze(formData.fields(0).entity.data.toByteArray))
	      }
	    }
	  }
  }
}