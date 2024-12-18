/*
    This component will be used to display a user's connection summary, their follower and following count
    This component will include a child component that will display a list of connections
    To see a list of the user's connections, click on the Followers or Following to display a list of their connections
    We will implement the list as a modal, by default the modal is not visible, but clicking on the target elements will change the state to visible and display the list
*/

import { useState } from "react";
import ConnectionList from "./ConnectionList";
import Modal from "react-modal";

const ConnectionDisplay = ({followers, following}) => {

    const [visible, setVisible] = useState(false);
    // list type is either followers or following
    const [listType, setListType] = useState("");

    const displayList = (type) => {
        setListType(type);
        setVisible(true);
    }

    const close = () => {
        setVisible(false);
        setListType("");
    }

    return (
        <>
        <h3>Hello Connection Display</h3>
        <span>Followers: {followers.length}</span>
        <button onClick={() => displayList("followers")}>View</button>
        <br/>
        <span>Following: {following.length}</span>
        <button onClick={() => displayList("following")}>View</button>
        <Modal isOpen={visible}>
            <div>
                <button onClick={close}>Close</button>
                <ConnectionList connections={listType == "followers" ? followers : following} title={listType == "followers" ? "Followers:" : "Following:"} />
            </div>   
        </Modal>
        </>
    );
}

export default ConnectionDisplay;