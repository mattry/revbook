import { Link } from 'react-router-dom';

const ConnectionList = ({connections, title}) => {

    return(
        <>
        <br/><br/>
        <h2 className='modal-title'>{title}</h2><br/>
        {connections.length > 0 ? (
                <>
                    {connections.map((connection) => (
                        <div key={connection.userId}>
                        <Link to={`/user/${connection.userId}`} className="user-link">
                                    {connection.firstName} {connection.lastName}
                        </Link>
                        <br/>
                        </div>
                    ))}
                </>
            ) : (
                <p>Nothing to display</p>
            )}
        </>
    );
}

export default ConnectionList;