(ns quotes.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [quotes.handlers]
              [quotes.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))
(defn alert [title]
  (.alert Alert title))

(def my-quotes ["When you find yourself in a hole,stop digging. — Will"
                "If you're waiting to have a good idea before you have any ideas, you won't have many ideas."
                "The most experienced planner in the world is your brain."
                "Don't just do something. Stand there. — Rochelle Myer"
                "People love to win. If you're not totally clear about the purpose of what you're doing, you have no chance of winning."
                "Fanaticism Consists of redoubling your efforts when you have forgotten your aim. — George"
                "Celebrate any progress. Don't wait to get perfect. — Ann McGee Cooper"
                "If you're not sure why you're doing something, you can never do enough of it."])
(def quotes-icon (js/require "./assets/images/quotes-icon.png"))

(defn quote-main []
  [view {:style {:margin 15 :flex 1 :justify-content "center" :align-items "center"}}
   [image {:source quotes-icon
           :style {:width "100%" :height 180}}]
   [text {:style {:font-size 24 :font-weight "bold" :font-style "italic"
                  :text-align "center" :margin-top 20}}
    (rand-nth my-quotes)]])

(defn og-app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [image {:source (js/require "./assets/images/cljs.png")
               :style {:width 200
                       :height 200}}]
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(alert "HELLO!")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press"]]])))

(defn app-root []
  (fn []
    [quote-main]))


(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
