package groovy.swing

import groovy.util.GroovySwingTestCase
import groovy.swing.factory.SwingWorkerFactory

class SwingWorkerTest extends GroovySwingTestCase {
   def builder

   public void setUp() {
       builder = new SwingXBuilder()
   }

   public void testWorker_answerDirectly() {
      if( isHeadless() ) return

      def theUltimateAnswer = 0
      def deepThought = builder.withWorker {
         work {
            Thread.sleep 300
            42
         }

         onDone {
            theUltimateAnswer = get() 
         }
      }

      assert theUltimateAnswer == 0
      deepThought.execute()
      Thread.sleep 500
      assert theUltimateAnswer == 42
   }

   public void testWorker_answerWithLocalClosure() {
      if( isHeadless() ) return

      def theUltimateAnswer = 0
      def deepThought = builder.withWorker {
         def compute = { 42 }

         work {
            Thread.sleep 300
            compute()
         }

         onDone {
            theUltimateAnswer = get() 
         }
      }

      assert theUltimateAnswer == 0
      deepThought.execute()
      Thread.sleep 500
      assert theUltimateAnswer == 42
   }

   public void testWorker_answerWithGlobalClosure() {
      if( isHeadless() ) return

      def compute = { 42 }
      def theUltimateAnswer = 0
      def deepThought = builder.withWorker {
         work {
            Thread.sleep 300
            compute()
         }

         onDone {
            theUltimateAnswer = get() 
         }
      }

      assert theUltimateAnswer == 0
      deepThought.execute()
      Thread.sleep 500
      assert theUltimateAnswer == 42
   }

   public void testWorker_answerWithLocalVar() {
      if( isHeadless() ) return

      def theUltimateAnswer = 0
      def deepThought = builder.withWorker {
         def answer = 42

         work {
            Thread.sleep 300
            answer
         }

         onDone {
            theUltimateAnswer = get() 
         }
      }

      assert theUltimateAnswer == 0
      deepThought.execute()
      Thread.sleep 500
      assert theUltimateAnswer == 42
   }

   public void testWorker_answerWithGlobalVar() {
      if( isHeadless() ) return

      def answer = 42
      def theUltimateAnswer = 0
      def deepThought = builder.withWorker {
         work {
            Thread.sleep 300
            return answer
         }

         onDone {
            theUltimateAnswer = get() 
         }
      }

      assert theUltimateAnswer == 0
      deepThought.execute()
      Thread.sleep 500
      assert theUltimateAnswer == 42
   }

   public void testWorker_answerWithBuilderVars() {
      if( isHeadless() ) return

      builder.answer = 42
      def theUltimateAnswer = 0
      def deepThought = builder.withWorker {
         work {
            Thread.sleep 300
            answer
         }

         onDone {
            theUltimateAnswer = get() 
            theUltimateQuestion = "unknown"
         }
      }

      assert theUltimateAnswer == 0
      shouldFail( MissingPropertyException ){
         assert builder.theUltimateQuestion == null
      }
      deepThought.execute()
      Thread.sleep 500
      assert theUltimateAnswer == 42
      assert builder.theUltimateQuestion == "unknown"
   }

   public void testWorker_publishAndProcess() {
      if( isHeadless() ) return

      def processed = []
      def deepThought = builder.withWorker {
         work {
            (0..9).inject([]) { l, v -> 
               Thread.sleep 100
               publish( v )
               l << v
            }
         }

         onUpdate { List<Object> chunks ->
            chunks.each { processed << it }
         }

         onDone {
            answer = get() 
         }
      }

      deepThought.execute()
      Thread.sleep 100 * 13
      assert builder.answer == [0,1,2,3,4,5,6,7,8,9]
      assert processed == [0,1,2,3,4,5,6,7,8,9]
   }
}
