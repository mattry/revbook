import { Link } from 'react-router-dom';

const ReactionsList = ({ reactions, title }) => {
    return (
      <>
      <br/><br/>
        <h2 className='modal-title'>{title}</h2><br/>
        {reactions.length > 0 ? (
          <>
            {reactions.map((reaction) => (
              <div key={reaction.reacterId}>
              <Link to={`/user/${reaction.reacterId}`} className="user-link">
                {reaction.firstName} {reaction.lastName}
              </Link><br/>
              </div>
            ))}
          </>
        ) : (
          <p>No reactions to display</p>
        )}
      </>
    );
  };
  
  export default ReactionsList;