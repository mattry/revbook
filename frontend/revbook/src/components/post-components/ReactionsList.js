import { Link } from 'react-router-dom';

const ReactionsList = ({ reactions, title }) => {
    return (
      <>
      <br/><br/>
        <h2 className='modal-title'>{title}</h2><br/>
        {reactions.length > 0 ? (
          <p>
            {reactions.map((reaction) => (
              <Link to={`/user/${reaction.reacterId}`} className="user-link">
                {reaction.firstName} {reaction.lastName}
              </Link>
            ))}
          </p>
        ) : (
          <p>No reactions to display</p>
        )}
      </>
    );
  };
  
  export default ReactionsList;