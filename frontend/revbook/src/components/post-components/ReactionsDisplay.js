import { useState, useEffect } from "react";
import Modal from "react-modal";
import ReactionsList from "./ReactionsList";
import axios from "axios";
import { useUser } from "../UserContext";

axios.defaults.withCredentials = true;

const ReactionsDisplay = ({ entityId, entityType }) => {
  const [reactions, setReactions] = useState([]); // Stores reactions
  const [reactionType, setReactionType] = useState(""); // Tracks modal type ("LIKE" or "DISLIKE")
  const [userReaction, setUserReaction] = useState(null); // Tracks the user's own reaction
  const [visible, setVisible] = useState(false); // Modal visibility
  const { user } = useUser(); // Current user from context

  // Fetch reactions (all likes and dislikes for the given entity)
  const getReactions = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/${entityType}s/${entityId}/reactions`);
      setReactions(response.data);
      // Find current user's reaction if it exists
      const existingReaction = response.data.find((reaction) => reaction.reacterId === user.userId);
      setUserReaction(existingReaction?.reactionType || null);
    } catch (error) {
      console.error(`Error getting reactions for ${entityType}:`, error);
    }
  };

  // React to entity (like or dislike)
  const react = async (type) => {
    try {
      await axios.post(`http://localhost:8080/${entityType}s/${entityId}/reactions`, {
        reactionType: type,
        reacterId: user.userId,
      });
      await getReactions(); // Refresh reactions after reacting
    } catch (error) {
      console.error(`Error reacting to ${entityType}:`, error);
    }
  };

  // Open modal based on clicked reaction type
  const openModal = (type) => {
    setReactionType(type);
    setVisible(true);
  };

  // Close modal
  const closeModal = () => {
    setVisible(false);
    setReactionType("");
  };

  useEffect(() => {
    getReactions();
  }, []);

  // Count likes and dislikes
  const likesCount = reactions.filter((reaction) => reaction.reactionType === "LIKE").length;
  const dislikesCount = reactions.filter((reaction) => reaction.reactionType === "DISLIKE").length;

  return (
    <>
      <div className="reaction-display">
        {/* Display the number of likes */}
        <span
          className="likes-count"
          onClick={() => openModal("LIKE")}
        >
          Likes: {likesCount}
        </span>

        {/* Display the number of dislikes */}
        <span
          className="dislikes-count"
          onClick={() => openModal("DISLIKE")}
        >
          Dislikes: {dislikesCount}
        </span>

        {/* Reaction buttons */}
        <div className="reaction-buttons">
          <button
            onClick={() => react("LIKE")}
            className={`reaction-button ${
              userReaction === "LIKE" ? "reaction-button-clicked" : ""
            }`}
          >
            Like
          </button>
          <button
            onClick={() => react("DISLIKE")}
            className={`reaction-button ${
              userReaction === "DISLIKE" ? "reaction-button-clicked" : ""
            }`}
          >
            Dislike
          </button>
        </div>
      </div>

      {/* Modal */}
      <Modal isOpen={visible} ariaHideApp={false}>
        <div>
          <button onClick={closeModal}>Close</button>
          <ReactionsList
            reactions={reactions.filter((reaction) => reaction.reactionType === reactionType)}
            title={reactionType === "LIKE" ? "Users who liked this:" : "Users who disliked this:"}
          />
        </div>
      </Modal>
    </>
  );
};

export default ReactionsDisplay;